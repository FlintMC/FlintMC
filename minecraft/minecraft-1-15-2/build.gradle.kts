import net.flintmc.gradle.extension.FlintGradleExtension.Type
import net.flintmc.minecraft.version.MinecraftVersionGenerator
import java.nio.file.Paths

plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    type = Type.LIBRARY
    staticFileEntry(
        Paths.get("build/generated/flint/version.json"),
        Paths.get("versions/flint-1.15.2/flint-1.15.2.json"),
        "version.json"
    )
}

afterEvaluate {
    tasks {
        create("generateVersionJson") {
            doLast {
                val minecraftVersion = MinecraftVersionGenerator.generateWithProjectDependencies(
                    gameVersion = "1.15.2",
                    versionFile = file("version.json"),
                    project = project.project(":bootstrap")
                )

                minecraftVersion.mainClass = "net.flintmc.bootstrap.Bootstrap"
                minecraftVersion.id = "flint-1.15.2"

                val version = file("build/generated/flint/version.json")
                version.parentFile.mkdirs()
                version.writeBytes(
                    MinecraftVersionGenerator.objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsBytes(minecraftVersion)
                )
            }
        }

        "publishFlintStaticFiles" {
            dependsOn("generateVersionJson")
        }
    }
}

dependencies {
    runtimeOnly(project(":bootstrap"))
}

