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

package net.flintmc.framework.generation.internal.parsing.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.internal.parsing.DefaultDataField;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;

/** {@inheritDoc} */
public class DefaultDataFactoryMethod implements DataFactoryMethod {

  private final CtMethod interfaceMethod;

  private final Collection<DataField> targetDataFields;

  public DefaultDataFactoryMethod(CtMethod interfaceMethod)
      throws ClassNotFoundException, NotFoundException {
    this.interfaceMethod = interfaceMethod;
    this.targetDataFields = this.parseDataFields();
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateImplementation(CtClass implementationClass, String dataImplementationName)
      throws CannotCompileException, NotFoundException {
    String rawMethodSource =
        "public %s %s (%s){%s dataInstance = new %s(); %s return dataInstance;}";

    String parameters =
        this.targetDataFields.stream()
            .map(dataField -> dataField.getType().getName() + " " + dataField.getName())
            .collect(Collectors.joining(","));

    String fieldInitialization =
        this.targetDataFields.stream()
            .map(
                dataField ->
                    String.format(
                        "dataInstance.%s = %s;", dataField.getName(), dataField.getName()))
            .collect(Collectors.joining());

    return CtNewMethod.make(
        String.format(
            rawMethodSource,
            this.interfaceMethod.getReturnType().getName(),
            this.interfaceMethod.getName(),
            parameters,
            dataImplementationName,
            dataImplementationName,
            fieldInitialization),
        implementationClass);
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod getInterfaceMethod() {
    return this.interfaceMethod;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<DataField> getTargetDataFields() {
    return this.targetDataFields;
  }

  private Collection<DataField> parseDataFields() throws ClassNotFoundException, NotFoundException {
    Collection<DataField> dataFields = new ArrayList<>();

    Object[][] allParameterAnnotations = this.interfaceMethod.getParameterAnnotations();

    for (int parameterIndex = 0;
        parameterIndex < allParameterAnnotations.length;
        parameterIndex++) {
      Object[] parameterAnnotations = allParameterAnnotations[parameterIndex];

      for (Object parameterAnnotation : parameterAnnotations) {
        if (parameterAnnotation instanceof TargetDataField) {
          TargetDataField targetDataField = (TargetDataField) parameterAnnotation;
          CtClass parameterType = this.interfaceMethod.getParameterTypes()[parameterIndex];

          dataFields.add(new DefaultDataField(targetDataField.value(), parameterType));
        }
      }
    }

    if (dataFields.size() < allParameterAnnotations.length) {
      throw new IllegalStateException(
          String.format(
              "Not all parameters of create method %s in factory interface %s target a data field",
              this.interfaceMethod.getName(), this.interfaceMethod.getDeclaringClass().getName()));
    }

    return dataFields;
  }
}
