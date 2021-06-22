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

repositories {
    mavenLocal()
    maven {
        setUrl("https://dist.labymod.net/api/v1/maven/release")
        name = "Flint"
    }
    mavenCentral()
}

dependencies {
    annotationProcessor("com.google.auto.service", "auto-service", "1.0-rc6")
    implementation("com.google.auto.service", "auto-service", "1.0-rc6")
    implementation("net.flintmc", "flint-gradle", "2.12.0")
    implementation("org.ow2.asm", "asm", "9.1")
    implementation("org.ow2.asm", "asm-tree", "9.1")
    implementation("org.ow2.asm", "asm-commons", "9.1")
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.10.0-RC1")
    implementation(project(":util:util-mapping"))
    implementation(gradleApi())

}
