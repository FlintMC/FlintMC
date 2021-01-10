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

rootProject.name = "flint"

fun defineModule(path: String) {
    include(path)
    findProject(":$path")?.name = path.replace(":", "-")
}

pluginManagement {
    plugins {
        id("net.flintmc.flint-gradle") version "2.7.3"
    }

    buildscript {
        dependencies {
            classpath("net.flintmc", "flint-gradle", "2.7.3")
        }
        repositories {
            mavenLocal()
            maven {
                setUrl("https://dist.labymod.net/api/v1/maven/release")
                name = "Flint"
            }
            mavenCentral()
        }
    }

}


defineModule("annotation-processing:autoload")

defineModule("framework:eventbus")
defineModule("framework:inject")
defineModule("framework:inject-primitive")
defineModule("framework:packages")
defineModule("framework:service")
defineModule("framework:stereotype")
defineModule("framework:config")

defineModule(":launcher")

defineModule(":mcapi")

defineModule("render:gui")
defineModule("render:shader")
defineModule("render:vbo-rendering")
defineModule("render:webgui")
defineModule("render:model-renderer")

defineModule("transform:asm")
defineModule("transform:exceptions")
defineModule("transform:hook")
defineModule("transform:javassist")
defineModule("transform:launcher-plugin")
defineModule("transform:minecraft")
defineModule("transform:minecraft-obfuscator")
defineModule("transform:shadow")

defineModule("util:attribute")
defineModule("util:commons")
defineModule("util:csv")
defineModule("util:i18n")
defineModule("util:mapping")
defineModule("util:math")
defineModule("util:property")
defineModule("util:mojang")
defineModule("util:session-service")
defineModule("util:task-executor")

defineModule("minecraft:minecraft-1-15-2")
defineModule("minecraft:minecraft-1-16-4")
defineModule("bootstrap")
