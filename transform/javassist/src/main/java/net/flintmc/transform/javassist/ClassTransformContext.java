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

package net.flintmc.transform.javassist;

import javassist.*;

public interface ClassTransformContext {

  /**
   * Retrieves a field by name.
   *
   * @param name A field name.
   * @return A field.
   * @throws NotFoundException If the field could not be found.
   */
  CtField getField(String name) throws NotFoundException;

  /**
   * Adds a method to the class.
   *
   * @param returnType A return type.
   * @param name       A method name.
   * @param body       Method source code.
   * @param modifiers  Method access modifiers.
   * @return A method.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Adds a method to the class.
   *
   * @param src Method source code.
   * @return A method.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtMethod addMethod(String src) throws CannotCompileException;

  /**
   * Retrieves an owner method by name.
   *
   * @param name A method name.
   * @param desc A method descriptor.
   * @return A method.
   * @throws NotFoundException If the method could not be found.
   */
  CtMethod getOwnerMethod(String name, String desc) throws NotFoundException;

  /**
   * Adds a field to the class.
   *
   * @param type      Field type.
   * @param name      Field name.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(Class<?> type, String name, Modifier... modifiers) throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type      Field type.
   * @param name      Field name.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type      Field type.
   * @param name      Field name.
   * @param value     Initial field value.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(Class<?> type, String name, String value, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type      Field type.
   * @param name      Field name.
   * @param value     Initial field value.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(String type, String name, String value, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Retrieves a method by name and descriptor.
   *
   * @param name    Method name.
   * @param classes Method parameters.
   * @return A method.
   * @throws NotFoundException If the method could not be found.
   */
  CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException;

  /**
   * Retrieves the class.
   *
   * @return A class.
   */
  CtClass getCtClass();

  //@AssistedFactory(ClassTransformContext.class)
  interface Factory {
    ClassTransformContext create(CtClass ctClass);
  }
}
