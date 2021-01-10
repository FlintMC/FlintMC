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

dependencies {
    minecraft("1.15.2", "1.16.4") {
        annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    }
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-eventbus"))
    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    api(project(":framework:framework-config"))

    api(project(":transform:transform-hook"))
    api(project(":transform:transform-shadow"))
    api(project(":render:render-model-renderer"))
    api(project(":render:render-model-renderer", "internal"))

    api(project(":render:render-gui"))
    api(project(":render:render-webgui"))

    api(project(":util:util-i18n"))

    api("com.google.code.gson", "gson", "2.8.6")
}
