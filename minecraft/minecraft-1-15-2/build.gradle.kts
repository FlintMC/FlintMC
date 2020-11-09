import net.flintmc.minecraft.version.MinecraftVersionGenerator
import java.nio.file.Paths
import java.net.URL

plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    staticFileEntry(
        Paths.get("build/generated/flint/version.json"),
        Paths.get("versions/flint-1.15.2/flint-1.15.2.json"),
        "version.json"
    )
    urlFileEntry(
        URL("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.15.2-20200515.085601/mcp_config-1.15.2-20200515.085601.zip!/config/joined.tsrg"),
        Paths.get("flint/assets/1.15.2/joined.tsrg")
    )
    urlFileEntry(
        URL("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/fields.csv"),
        Paths.get("flint/assets/1.15.2/fields.csv")
    )
    urlFileEntry(
        URL("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/params.csv"),
        Paths.get("flint/assets/1.15.2/params.csv")
    )
    urlFileEntry(
        URL("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/methods.csv"),
        Paths.get("flint/assets/1.15.2/methods.csv")
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

