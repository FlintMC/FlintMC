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

package net.flintmc.framework.generation.internal.parsing;

import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.internal.parsing.data.DataGetter;
import net.flintmc.framework.generation.internal.parsing.data.DataSetter;
import net.flintmc.framework.generation.internal.parsing.factory.DefaultDataFactoryMethod;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.DataMethodParser;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;
import net.flintmc.framework.inject.implement.Implement;
import java.util.Collection;
import java.util.HashSet;

/** {@inheritDoc} */
@Singleton
@Implement(DataMethodParser.class)
public class DefaultDataMethodParser implements DataMethodParser {

  /** {@inheritDoc} */
  @Override
  public Collection<DataFactoryMethod> parseFactoryMethods(
      CtClass factoryInterface, CtClass dataInterface)
      throws NotFoundException, ClassNotFoundException {
    this.checkIsInterface(factoryInterface, dataInterface);

    Collection<DataFactoryMethod> methods = new HashSet<>();

    for (CtMethod interfaceMethod : factoryInterface.getDeclaredMethods()) {
      if (this.isDefault(interfaceMethod)) {
        // already implemented methods are ignored
        continue;
      }

      // all methods in the factory should return an instance of the data interface
      if (!interfaceMethod.getReturnType().equals(dataInterface)) {
        throw new IllegalStateException(
            String.format(
                "Method %s in data factory interface %s does not return an instance of the data interface",
                interfaceMethod.getName(), factoryInterface.getName()));
      }

      methods.add(new DefaultDataFactoryMethod(interfaceMethod));
    }

    return methods;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<DataFieldMethod> parseDataMethods(
      CtClass dataInterface, Collection<DataField> targetDataFields)
      throws NotFoundException, ClassNotFoundException {
    this.checkIsInterface(dataInterface);

    Collection<DataFieldMethod> methods = new HashSet<>();

    for (CtMethod interfaceMethod : dataInterface.getDeclaredMethods()) {
      if (this.isDefault(interfaceMethod)) {
        // already implemented methods are ignored
        continue;
      }

      DataField targetDataField = this.getTargetDataField(interfaceMethod, targetDataFields);

      CtClass returnType = interfaceMethod.getReturnType();

      if (interfaceMethod.getParameterTypes().length == 1
          && (returnType.equals(dataInterface)
              || returnType
                  .getName()
                  .equals("void"))) { // a setter can return the instance of the data interface too
        CtClass firstParameterType = interfaceMethod.getParameterTypes()[0];

        this.checkDataFieldMethodType(
            "setter", firstParameterType, interfaceMethod, dataInterface, targetDataField);

        methods.add(
            new DataSetter(interfaceMethod, targetDataField, returnType.equals(dataInterface)));
      } else if (interfaceMethod.getParameterTypes().length == 0
          && !returnType.getName().equals("void")) {
        this.checkDataFieldMethodType(
            "getter", returnType, interfaceMethod, dataInterface, targetDataField);
        methods.add(new DataGetter(interfaceMethod, targetDataField));
      } else {
        throw new IllegalStateException(
            String.format(
                "Method %s in data interface %s is neither a setter nor a getter but targets a data field",
                interfaceMethod.getName(), dataInterface.getName()));
      }
    }

    return methods;
  }

  private void checkIsInterface(CtClass... interfaces) {
    for (CtClass anInterface : interfaces) {
      if (!anInterface.isInterface()) {
        throw new IllegalStateException(
            String.format(
                "Data class or factory class %s is not an interface", anInterface.getName()));
      }
    }
  }

  private boolean isDefault(CtMethod method) {
    return (method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC))
        == Modifier.PUBLIC;
  }

  private DataField getTargetDataField(
      CtMethod interfaceMethod, Collection<DataField> targetDataFields)
      throws ClassNotFoundException {
    if (!interfaceMethod.hasAnnotation(TargetDataField.class)) {
      throw new IllegalStateException(
          String.format(
              "Method %s in data interface %s does not target a valid data field (Missing @TargetDataField)",
              interfaceMethod.getName(), interfaceMethod.getDeclaringClass().getName()));
    }

    String fieldName =
        ((TargetDataField) interfaceMethod.getAnnotation(TargetDataField.class)).value();

    DataField targetDataField = null;

    for (DataField dataClassField : targetDataFields) {
      if (dataClassField.getName().equals(fieldName)) {
        targetDataField = dataClassField;
        break;
      }
    }

    if (targetDataField == null) {
      throw new IllegalStateException(
          String.format(
              "Method %s in data interface %s does not target a valid data field (Missing @TargetDataField)",
              interfaceMethod.getName(), interfaceMethod.getDeclaringClass().getName()));
    }

    return targetDataField;
  }

  private void checkDataFieldMethodType(
      String methodDescription,
      CtClass methodType,
      CtMethod interfaceMethod,
      CtClass dataInterface,
      DataField targetDataField) {
    if (!methodType.equals(targetDataField.getType())) {
      throw new IllegalStateException(
          String.format(
              "The type (%s) of %s %s in data interface %s is not compatible with the type of the target data field %s (%s)",
              methodType.getName(),
              methodDescription,
              interfaceMethod.getName(),
              dataInterface.getName(),
              targetDataField.getName(),
              targetDataField.getType().getName()));
    }
  }
}
