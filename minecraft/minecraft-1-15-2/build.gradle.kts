import net.flintmc.minecraft.version.MinecraftVersionGenerator

plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    staticFiles {
        create("version.json") {
            from("build/generated/flint/version.json")
            to("versions/flint-1.15.2/flint-1.15.2.json")
        }

        create("joined.tsrg") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.15.2-20200515.085601/mcp_config-1.15.2-20200515.085601.zip!/config/joined.tsrg")
            to("flint/assets/1.15.2/joined.tsrg")
        }

        create("fields.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/fields.csv")
            to("flint/assets/1.15.2/fields.csv")
        }

        create("params.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/params.csv")
            to("flint/assets/1.15.2/params.csv")
        }

        create("methods.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20200610-1.15.1/mcp_snapshot-20200610-1.15.1.zip!/methods.csv")
            to("flint/assets/1.15.2/methods.csv")
        }
    }
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

        "generateStaticFileChecksums" {
            dependsOn("generateVersionJson")
        }
    }
}

dependencies {
    runtimeOnly(project(":bootstrap"))
}

