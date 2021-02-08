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
buildscript {
    repositories {
        maven {
            setUrl("https://dist.labymod.net/api/v1/maven/release")
            name = "Flint"
            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "Bearer CbtTjzAOuDBr5QXcGnBc1MB3eIHxcZetnyHtdN76VpTNgbwAf87bzWPCntsXwj52"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("net.flintmc.flint-gradle")
    id("net.minecrell.licenser") version "0.4.1"
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
        plugins.apply("net.minecrell.licenser")

        version = System.getenv().getOrDefault("VERSION", "2.0.22")

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
            header = rootProject.file("LICENSE-HEADER")
            include("**/*.java")
            include("**/*.kts")

            tasks {
                create("gradle") {
                    files = project.files("build.gradle.kts", "settings.gradle.kts")
                }
            }
        }
    }
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.apache.logging.log4j:log4j-api:2.8.2")
            force("com.google.guava:guava:27.0.1-jre")
            force("org.apache.commons:commons-lang3:3.10")
            force("org.apache.logging.log4j:log4j-core:2.8.2")
            force("it.unimi.dsi:fastutil:8.2.1")
            force("net.java.dev.jna:jna:4.4.0")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("com.google.code.gson:gson:2.8.6")
            force("commons-io:commons-io:2.6")
            force("commons-codec:commons-codec:1.10")
            force("com.beust:jcommander:1.78")
        }
    }

    tasks.withType<JavaCompile> {
        options.isFork = true
    }
}

flint {
    flintVersion = System.getenv().getOrDefault("VERSION", "1.0.0")

    projectFilter {
        !arrayOf(":", ":framework", ":render", ":transform", ":util", ":minecraft").contains(it.path)
    }

    minecraftVersions("1.15.2", "1.16.5")

    type = net.flintmc.gradle.extension.FlintGradleExtension.Type.LIBRARY
    authors = arrayOf("LabyMedia GmbH")

    runs {
        overrideMainClass("net.flintmc.launcher.FlintLauncher")
    }
}
