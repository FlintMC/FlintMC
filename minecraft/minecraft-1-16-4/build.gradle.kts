/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import net.flintmc.minecraft.version.MinecraftVersionGenerator

plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    staticFiles {
        create("version.json") {
            from("build/generated/flint/version.json")
            to("versions/flint-1.16.4/flint-version.json")
        }

        create("joined.tsrg") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.16.4/mcp_config-1.16.4.zip!/config/joined.tsrg")
            to("flint/assets/1.16.4/joined.tsrg")
        }

        create("fields.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20201028-1.16.3/mcp_snapshot-20201028-1.16.3.zip!/fields.csv")
            to("flint/assets/1.16.4/fields.csv")
        }

        create("params.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20201028-1.16.3/mcp_snapshot-20201028-1.16.3.zip!/params.csv")
            to("flint/assets/1.16.4/params.csv")
        }

        create("methods.csv") {
            from("jar:https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_snapshot/20201028-1.16.3/mcp_snapshot-20201028-1.16.3.zip!/methods.csv")
            to("flint/assets/1.16.4/methods.csv")
        }
    }
}

afterEvaluate {
    tasks {
        create("generateVersionJson") {
            doLast {
                val minecraftVersion = MinecraftVersionGenerator.generateWithProjectDependencies(
                    gameVersion = "1.16.4",
                    versionFile = file("version.json"),
                    project = project.project(":bootstrap")
                )

                minecraftVersion.mainClass = "net.flintmc.bootstrap.Bootstrap"
                minecraftVersion.id = "flint-1.16.4"

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

