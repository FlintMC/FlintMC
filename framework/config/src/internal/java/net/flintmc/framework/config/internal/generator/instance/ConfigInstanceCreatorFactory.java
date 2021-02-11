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

package net.flintmc.framework.config.internal.generator.instance;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.launcher.LaunchController;

@Singleton
public class ConfigInstanceCreatorFactory {

  private final ClassPool pool;
  private final AtomicInteger idCounter;
  private final Random random;

  private final CtClass instanceCreatorType;
  private final CtClass parsedConfigType;
  private final CtClass generatorType;
  private final CtClass generatingConfigType;

  @Inject
  private ConfigInstanceCreatorFactory(ClassPool pool) throws NotFoundException {
    this.pool = pool;
    this.idCounter = new AtomicInteger();
    this.random = new Random();

    this.instanceCreatorType = pool.get(ConfigInstanceCreator.class.getName());
    this.parsedConfigType = pool.get(ParsedConfig.class.getName());
    this.generatorType = pool.get(ConfigGenerator.class.getName());
    this.generatingConfigType = pool.get(GeneratingConfig.class.getName());
  }

  public ConfigInstanceCreator newCreator(ConfigGenerator generator, GeneratingConfig config)
      throws CannotCompileException, IOException, ReflectiveOperationException {
    CtClass generating = this.pool.makeClass(
        "ConfigInstanceCreator_" + this.idCounter.incrementAndGet() + "_" + this.random
            .nextInt(Integer.MAX_VALUE));

    generating.addInterface(this.instanceCreatorType);

    generating.addConstructor(CtNewConstructor.skeleton(
        new CtClass[]{this.generatorType, this.generatingConfigType}, new CtClass[0],
        generating));

    generating.addField(new CtField(this.generatorType, "generator", generating),
        CtField.Initializer.byParameter(0));
    generating.addField(new CtField(this.generatingConfigType, "config", generating),
        CtField.Initializer.byParameter(1));

    String src = String.format(
        "{ %s result = new %1$s(); this.generator.bindConfig(this.config, result); return result; }",
        config.getGeneratedImplementation(config.getBaseClass().getName()).getName());

    generating.addMethod(CtNewMethod.make(
        this.parsedConfigType, "newInstance", new CtClass[0], new CtClass[0],
        src, generating));

    return this.newInstance(generating, generator, config);
  }

  private ConfigInstanceCreator newInstance(CtClass target, Object... args)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    byte[] bytes = target.toBytecode();
    Class<?> generated = LaunchController.getInstance().getRootLoader()
        .commonDefineClass(target.getName(), bytes, 0, bytes.length, null);

    return (ConfigInstanceCreator) generated.getDeclaredConstructors()[0].newInstance(args);
  }
}
