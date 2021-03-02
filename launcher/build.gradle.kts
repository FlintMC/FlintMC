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
    api(project(":transform:transform-exceptions"))

    api("org.apache.logging.log4j", "log4j-api", "2.8.1")
    api("com.beust", "jcommander", "1.78")
    api("io.sentry", "sentry-log4j2", "1.7.30")
    api("commons-io", "commons-io", "2.6")

    runtimeOnly("org.apache.logging.log4j", "log4j-core", "2.8.1")
}
