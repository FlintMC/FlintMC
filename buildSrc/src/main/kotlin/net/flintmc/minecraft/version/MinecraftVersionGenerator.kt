package net.flintmc.minecraft.version

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import java.io.File

object MinecraftVersionGenerator {
    val objectMapper = JsonMapper.builder().addModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)


    fun generateWithProjectDependencies(
        gameVersion: String,
        versionFile: File,
        project: Project
    ): MinecraftVersion {

        val minecraftVersion = objectMapper.readValue(versionFile, MinecraftVersion::class.java)

        minecraftVersion.libraries.add(
            MinecraftVersion.Library(
                null,
                null,
                "net.flintmc:bootstrap:" + project.version.toString(),
                null,
                null
            )
        )
        (minecraftVersion.arguments["game"] as MutableList<Any>).addAll(arrayOf("--game-version", gameVersion))


        for (resolvedArtifact in project.configurations.getByName("manifest").resolvedConfiguration.resolvedArtifacts) {
            val componentIdentifier = resolvedArtifact.id.componentIdentifier
            if (componentIdentifier is ProjectComponentIdentifier) {
                val dependencyProject = project.project(componentIdentifier.projectPath)
                minecraftVersion.libraries.add(
                    MinecraftVersion.Library(
                        null,
                        null,
                        "${dependencyProject.group}:${dependencyProject.name}:${dependencyProject.version}${
                            if (resolvedArtifact.classifier == null)
                                ""
                            else
                                "-${resolvedArtifact.classifier}"
                        }",
                        null,
                        null
                    )
                )
            } else if (componentIdentifier is ModuleComponentIdentifier)
                minecraftVersion.libraries.add(
                    MinecraftVersion.Library(
                        null,
                        null,
                        "${componentIdentifier.group}:${componentIdentifier.module}:${componentIdentifier.version}${
                            if (resolvedArtifact.classifier == null)
                                ""
                            else
                                "-${resolvedArtifact.classifier}"
                        }",
                        null,
                        null
                    )
                )
        }

        return minecraftVersion
    }

}