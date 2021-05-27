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
    id("net.flintmc.flint-gradle")
    id("org.cadixdev.licenser") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

subprojects {
    plugins.withId("java") {
        apply<MavenPublishPlugin>()
        plugins.apply("org.cadixdev.licenser")

        version = System.getenv().getOrDefault("VERSION", "2.0.33")

        repositories {
            mavenCentral()
        }

        tasks.withType<JavaCompile> {
            options.isFork = true
        }

        tasks.test {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        license {
            header(rootProject.file("LICENSE-HEADER"))
            include("**/*.java")
            include("**/*.kts")

            tasks {
                create("gradle") {
                    files(project.files("build.gradle.kts", "settings.gradle.kts"))
                }
            }
        }

        java {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

tasks.javadoc {
    val sourceSets = subprojects
            .filter { it.pluginManager.hasPlugin("java") }
            .map { it.sourceSets.getByName("main") }

    setSource(sourceSets.map { it.allJava })
    classpath = files(sourceSets.flatMap { it.compileClasspath })
    setDestinationDir(file("docs/generated"))
}

flint {
    flintVersion = System.getenv().getOrDefault("VERSION", "2.0.33")

    projectFilter {
        !arrayOf(
                ":",
                ":framework",
                ":render",
                ":transform",
                ":util",
                ":minecraft"
        ).contains(it.path)
    }

    minecraftVersions("1.15.2", "1.16.5")

    type = net.flintmc.gradle.extension.FlintGradleExtension.Type.LIBRARY
    authors = arrayOf("LabyMedia GmbH")

    resolutionStrategy {
        forceDependency("org.apache.logging.log4j:log4j-api:2.14.0")
        forceDependency("org.apache.logging.log4j:log4j-core:2.14.0")
        forceDependency("com.beust:jcommander:1.81")
    }

    runs {
        overrideMainClass("net.flintmc.launcher.FlintLauncher")
    }
}
