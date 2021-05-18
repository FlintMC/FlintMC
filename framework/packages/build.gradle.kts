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
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    api(project(":util:util-commons"))
    api("com.google.code.gson", "gson", "2.8.6")
    api("net.flintmc.installer", "logic", "1.1.12")
    api("net.flintmc.installer", "logic-implementation", "1.1.12")

    internalImplementation(project(":framework:framework-inject", "internal"))
    internalImplementation(project(":framework:framework-service"))
    internalImplementation(project(":framework:framework-stereotype", "internal"))
    internalImplementation(project(":util:util-mapping"))
}
