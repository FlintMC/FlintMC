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

package net.flintmc.launcher.classloading;

import net.flintmc.launcher.classloading.common.CommonClassLoader;

import java.net.URL;

/**
 * Interface allowing other classloader implementations to act as a child of the root classloader.
 * This grants the ability to load transformed classes using a {@link RootClassLoader}.
 */
public interface ChildClassLoader extends CommonClassLoader {
  @Override
  default URL commonFindResource(String name, boolean forClassLoad) {
    return childFindResource(name, forClassLoad);
  }

  /**
   * Searches this classloader for resources. Its counterpart is the protected `findResource` method
   * of {@link ClassLoader}.
   *
   * @param name the name of the resource to find
   * @param forClassLoad true, if the searched resource will be used as for classloading. Usually
   *     this will be true when name ends with {@code ".class"}
   * @return the found url to the resource, or null if none was found
   */
  URL childFindResource(String name, boolean forClassLoad);
}
