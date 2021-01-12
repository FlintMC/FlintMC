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

package net.flintmc.framework.config.internal.generator.method.reference.invoker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.internal.generator.base.ConfigClassLoader;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ReferenceInvocationGenerator {

  private final ClassPool pool;
  private final AtomicInteger idCounter;
  private final Random random;
  private final ConfigClassLoader classLoader;

  @Inject
  private ReferenceInvocationGenerator(ImplementationGenerator generator, ClassPool pool) {
    this.pool = pool;
    this.idCounter = new AtomicInteger();
    this.random = new Random();

    this.classLoader = generator.getClassLoader();
  }

  public ReferenceInvoker generateInvoker(
      GeneratingConfig config, CtMethod[] path, CtMethod getter, CtMethod setter)
      throws CannotCompileException, NotFoundException, IOException, ReflectiveOperationException {
    CtClass target =
        this.pool.makeClass(
            "ReferenceInvoker_"
                + this.idCounter.incrementAndGet()
                + "_"
                + this.random.nextInt(Integer.MAX_VALUE));
    target.addInterface(this.pool.get(ReferenceInvoker.class.getName()));

    String base =
        "((" + config.getGeneratedImplementation(config.getBaseClass().getName()).getName()
            + ") instance)";
    String lastAccessor = this.buildPathCasts(path) + base + this.buildPathGetters(path);

    target.addMethod(this.generateGetter(lastAccessor, target, getter));
    target.addMethod(this.generateSetter(lastAccessor, target, setter));

    return this.newInstance(target);
  }

  private ReferenceInvoker newInstance(CtClass target)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    Class<?> generated = this.classLoader.defineClass(target.getName(), target.toBytecode());

    return (ReferenceInvoker) generated.getDeclaredConstructor().newInstance();
  }

  private String buildPathCasts(CtMethod[] path) throws NotFoundException {
    StringBuilder builder = new StringBuilder();

    for (int i = path.length - 1; i >= 0; i--) {
      CtMethod method = path[i];

      builder.append("((").append(method.getReturnType().getName()).append(')');
    }

    return builder.toString();
  }

  private String buildPathGetters(CtMethod[] path) {
    StringBuilder builder = new StringBuilder();

    for (CtMethod method : path) {
      builder.append('.').append(method.getName()).append("())");
    }

    return builder.toString();
  }

  private CtMethod generateGetter(String lastAccessor, CtClass declaring, CtMethod getter)
      throws CannotCompileException, NotFoundException {
    String valueSrc =
        PrimitiveTypeLoader.asWrappedPrimitiveSource(
            getter.getReturnType(), lastAccessor + "." + getter.getName() + "()");
    String src = "public Object getValue(Object instance) {" + "return " + valueSrc + ";" + "}";

    return CtNewMethod.make(src, declaring);
  }

  private CtMethod generateSetter(String lastAccessor, CtClass declaring, CtMethod setter)
      throws CannotCompileException, NotFoundException {
    CtClass type = setter.getParameterTypes()[0];
    String valueSrc =
        "((" + type.getName() + ")" + PrimitiveTypeLoader.asPrimitiveSource(type, "newValue") + ")";

    String src =
        "public void setValue(Object instance, Object newValue) {"
            + lastAccessor
            + "."
            + setter.getName()
            + "("
            + valueSrc
            + ");"
            + "}";

    return CtNewMethod.make(src, declaring);
  }
}
