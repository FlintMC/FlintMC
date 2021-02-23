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

package net.flintmc.framework.config.internal.generator.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.function.Function;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.SignatureAttribute.ClassType;
import javassist.bytecode.SignatureAttribute.MethodSignature;
import javassist.bytecode.SignatureAttribute.TypeArgument;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigMultiGetterSetter;
import net.flintmc.framework.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

@Singleton
public class GenericMethodHelper {

  private final ClassPool pool;
  private final ConfigMethodInfo.Factory infoFactory;
  private final ConfigMultiGetterSetter.Factory multiFactory;
  private final Logger logger;

  @Inject
  private GenericMethodHelper(
      @InjectLogger Logger logger,
      ClassPool pool,
      ConfigMethodInfo.Factory infoFactory,
      ConfigMultiGetterSetter.Factory multiFactory) {
    this.logger = logger;
    this.pool = pool;
    this.infoFactory = infoFactory;
    this.multiFactory = multiFactory;
  }

  public ConfigMethod generateMultiGetterSetter(
      GeneratingConfig config,
      CtClass declaringClass,
      CtMethod method,
      Function<MethodSignature, ClassType> signatureMapper,
      String name)
      throws NotFoundException {
    CtClass objectType = this.pool.get(Object.class.getName());
    CtClass keyType = objectType;
    CtClass valueType = objectType;

    try {
      ClassType genericReturnType =
          signatureMapper.apply(SignatureAttribute.toMethodSignature(method.getGenericSignature()));
      TypeArgument[] arguments = genericReturnType.getTypeArguments();
      if (arguments.length != 0) {
        if (!arguments[0].isWildcard()) {
          keyType = this.pool.get(((ClassType) arguments[0].getType()).getName());
        }
        if (!arguments[1].isWildcard()) {
          valueType = this.pool.get(((ClassType) arguments[1].getType()).getName());
        }
      }
    } catch (BadBytecode exception) {
      this.logger.error(
          "Got bad bytecode while reading the generic signature of "
              + method.getDeclaringClass().getName()
              + "."
              + method.getName(),
          exception);
      return null;
    }

    ConfigMethodInfo info = this.infoFactory.create(
        config, declaringClass, name, method.getReturnType());
    return this.multiFactory.create(info, keyType, valueType);
  }
}
