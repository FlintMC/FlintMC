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

plugins {
    id("java-library")
}

group = "net.flintmc"

configurations {
    create("manifest")
}


dependencies {
    val manifest = configurations.getByName("manifest")
    configurations.getByName("implementation").extendsFrom(manifest)

    project.rootProject.subprojects.forEach { subProject ->
        if (!arrayOf(
                        ":",
                        ":annotation-processing",
                        ":annotation-processing:annotation-processing-autoload",
                        ":framework",
                        ":render",
                        ":transform",
                        ":util",
                        ":minecraft",
                        ":minecraft:minecraft-minecraft-1-15-2",
                        ":minecraft:minecraft-minecraft-1-16-5",
                        ":bootstrap"
                ).contains(subProject.path)
        ) {
            manifest(subProject)
            subProject.configurations.getByName("runtimeClasspath")
                    .allDependencies.forEach {
                        manifest(it)
                    }
        }
    }

    manifest("net.flintmc.installer", "logic", "1.1.5")
    manifest("net.flintmc.installer", "logic-implementation", "1.1.5")
    manifest("com.google.code.gson", "gson", "2.8.6")

}