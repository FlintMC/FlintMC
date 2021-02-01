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

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import javassist.ClassPool;
import javassist.CtClass;
import net.flintmc.framework.generation.DataImplementationGenerator;
import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.DataMethodParser;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.launcher.LaunchController;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service(DataFactory.class)
public class DataService implements ServiceHandler<DataFactory> {

  private final DataMethodParser dataMethodParser;

  private final DataImplementationGenerator dataImplementationGenerator;

  private final ClassPool classPool;

  @Inject
  public DataService(
      DataMethodParser dataMethodParser,
      DataImplementationGenerator dataImplementationGenerator,
      ClassPool classPool) {
    this.dataMethodParser = dataMethodParser;
    this.dataImplementationGenerator = dataImplementationGenerator;
    this.classPool = classPool;
  }

  @Override
  public void discover(AnnotationMeta<DataFactory> meta) throws ServiceNotFoundException {
    CtClass factoryInterface = meta.getClassIdentifier().getLocation();

    try {
      // the factory defines which data class it creates
      CtClass dataInterface = this.classPool.get(meta.getAnnotation().value().getName());

      // parsing all factory methods which define the data fields by their parameters
      Collection<DataFactoryMethod> dataFactoryMethods =
          this.dataMethodParser.parseFactoryMethods(factoryInterface, dataInterface);

      // collecting all data fields from all factory methods
      Collection<DataField> targetDataFields = new HashSet<>();
      for (DataFactoryMethod dataFactoryMethod : dataFactoryMethods) {
        targetDataFields.addAll(dataFactoryMethod.getTargetDataFields());
      }

      // parsing all wanted getter/setter methods from the data interface
      Collection<DataFieldMethod> dataFieldMethods =
          this.dataMethodParser.parseDataMethods(dataInterface, targetDataFields);

      String dataImplementationName =
          "Default"
              + dataInterface.getSimpleName()
              + UUID.randomUUID().toString().replaceAll("-", "");

      CtClass dataClass =
          this.dataImplementationGenerator.generateDataImplementationClass(
              dataInterface, dataImplementationName, targetDataFields, dataFieldMethods);

      // loading the data interface class again
      CtResolver.get(dataInterface);
      // just defining and loading the data class, nothing else is needed
      this.defineClass(dataClass.getName(), dataClass.toBytecode());

      CtClass factoryClass =
          this.dataImplementationGenerator.generateFactoryImplementationClass(
              factoryInterface, dataFactoryMethods, dataImplementationName);

      // creating the factory singleton and binding it to the interface with Guice
      Class<Object> resolvedFactoryInterface = CtResolver.get(factoryInterface);
      Object factoryInstance =
          this.defineClass(factoryClass.getName(), factoryClass.toBytecode())
              .getDeclaredConstructor()
              .newInstance();

      InjectionHolder.getInstance()
          .addModules(
              new AbstractModule() {
                @Override
                protected void configure() {
                  super.bind(resolvedFactoryInterface).toInstance(factoryInstance);
                }
              });

    } catch (Exception exception) {
      throw new ServiceNotFoundException(
          String.format(
              "Unable to create an implementation of factory interface %s",
              factoryInterface.getName()),
          exception);
    }
  }

  private Class<?> defineClass(String name, byte[] byteCode) {
    return LaunchController.getInstance()
        .getRootLoader()
        .commonDefineClass(name, byteCode, 0, byteCode.length, null);
  }
}
