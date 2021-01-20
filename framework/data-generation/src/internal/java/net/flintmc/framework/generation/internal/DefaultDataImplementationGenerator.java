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

package net.flintmc.framework.generation.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.DataImplementationGenerator;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Singleton
@Implement(DataImplementationGenerator.class)
public class DefaultDataImplementationGenerator implements DataImplementationGenerator {

  private final ClassPool classPool;

  @Inject
  public DefaultDataImplementationGenerator(ClassPool classPool) {
    this.classPool = classPool;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass generateDataImplementationClass(
      CtClass dataInterface,
      String dataImplementationName,
      Collection<DataField> targetDataFields,
      Collection<DataFieldMethod> dataFieldMethods)
      throws CannotCompileException, NotFoundException {
    CtClass dataImplementationClass = this.classPool.makeClass(dataImplementationName);
    dataImplementationClass.addInterface(dataInterface);

    // adding all parsed data fields
    for (DataField targetDataField : targetDataFields) {
      dataImplementationClass.addField(targetDataField.generate(dataImplementationClass));
    }

    // implementing all methods from the data interface
    for (DataFieldMethod dataFieldMethod : dataFieldMethods) {
      dataImplementationClass.addMethod(
          dataFieldMethod.generateImplementation(dataImplementationClass));
    }

    return dataImplementationClass;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass generateFactoryImplementationClass(
      CtClass factoryInterface,
      Collection<DataFactoryMethod> dataFactoryMethods,
      String dataImplementationName)
      throws CannotCompileException, NotFoundException {
    CtClass factoryImplementationClass =
        this.classPool.makeClass(
            "Default"
                + factoryInterface.getSimpleName()
                + UUID.randomUUID().toString().replaceAll("-", ""));
    factoryImplementationClass.addInterface(factoryInterface);

    // implementing all create methods from the factory interface
    for (DataFactoryMethod dataFactoryMethod : dataFactoryMethods) {
      factoryImplementationClass.addMethod(
          dataFactoryMethod.generateImplementation(
              factoryImplementationClass, dataImplementationName));
    }

    return factoryImplementationClass;
  }
}
