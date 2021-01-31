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

package net.flintmc.transform.launchplugin;

import net.flintmc.transform.exceptions.ClassTransformException;

public interface LateInjectedTransformer {

  /**
   * Transform a class.
   *
   * @param className A class name.
   * @param classData A byte array reassembling a class.
   * @return A derivative of class data.
   * @throws ClassTransformException If the class transformation failed.
   */
  default byte[] transform(String className, byte[] classData) throws ClassTransformException {
    return classData;
  }

  /**
   * This method will be invoked after a class has been transformed by a transformer to use the
   * changes, at this point no more changes can be made to the class.
   *
   * @param transformer The non-null transformer that transformed the class
   * @param className   The non-null name of the class that has been transformed
   * @param classData   The non-null data of the modified class
   */
  default void notifyTransform(LateInjectedTransformer transformer, String className,
      byte[] classData) {
  }
}
