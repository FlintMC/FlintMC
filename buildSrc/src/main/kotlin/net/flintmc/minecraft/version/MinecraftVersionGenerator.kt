package net.flintmc.minecraft.version

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import java.io.File
import java.lang.IllegalArgumentException

object MinecraftVersionGenerator {
    val objectMapper = JsonMapper.builder().addModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)


    fun generateWithProjectDependencies(
        versionFile: File,
        project: Project
    ): MinecraftVersion {

        val minecraftVersion = objectMapper.readValue(versionFile, MinecraftVersion::class.java)

        for (resolvedArtifact in project.configurations.getByName("runtimeClasspath").resolvedConfiguration.resolvedArtifacts) {
            val componentIdentifier = resolvedArtifact.id.componentIdentifier
            minecraftVersion.libraries.add(
                when (componentIdentifier) {
                    is ProjectComponentIdentifier -> {
                        val targetProject = project.project(componentIdentifier.projectPath)
                        MinecraftVersion.Library(
                            null,
                            null,
                            "${targetProject.group}:${targetProject.name}:${targetProject.version}${
                                if (resolvedArtifact.classifier != null)
                                    "-${resolvedArtifact.classifier}"
                                else
                                    ""
                            }",
                            null,
                            null
                        )
                    }
                    is ModuleComponentIdentifier -> {
                        MinecraftVersion.Library(
                            null,
                            null,
                            "${componentIdentifier.group}:${componentIdentifier.module}:${componentIdentifier.version}${
                                if (resolvedArtifact.classifier != null)
                                    "-${resolvedArtifact.classifier}"
                                else
                                    ""
                            }",
                            null,
                            null
                        )
                    }
                    else -> throw IllegalArgumentException("Invalid dependency type $componentIdentifier")
                }
            )
        }
        return minecraftVersion
    }
}