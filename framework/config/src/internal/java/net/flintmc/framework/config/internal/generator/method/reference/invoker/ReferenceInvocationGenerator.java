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
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;
import net.flintmc.launcher.LaunchController;

@Singleton
public class ReferenceInvocationGenerator {

  private static final String REFERENCE_CLASS = ConfigObjectReference.class.getName();

  private final ClassPool pool;
  private final AtomicInteger idCounter;
  private final Random random;

  @Inject
  private ReferenceInvocationGenerator(ClassPool pool) {
    this.pool = pool;
    this.idCounter = new AtomicInteger();
    this.random = new Random();
  }

  public ReferenceInvoker generateInvoker(
      GeneratingConfig config,
      String configName,
      CtMethod[] path,
      CtMethod getter,
      CtMethod setter)
      throws CannotCompileException, NotFoundException, IOException, ReflectiveOperationException {
    CtClass generating =
        this.pool.makeClass(
            "ReferenceInvoker_"
                + this.idCounter.incrementAndGet()
                + "_"
                + this.random.nextInt(Integer.MAX_VALUE));
    generating.addInterface(this.pool.get(ReferenceInvoker.class.getName()));

    CtClass targetClass = config.getGeneratedImplementation(
        config.getBaseClass().getName(), config.getBaseClass());

    String base = "((" + targetClass.getName() + ") $1)";
    String lastAccessor = this.buildPathCasts(config, path) + base + this.buildPathGetters(path);

    generating.addMethod(this.generateGetter(lastAccessor, generating, getter));
    generating.addMethod(this.generateSetter(lastAccessor, generating, setter));
    generating.addMethod(this.generateReferenceSetter(lastAccessor, generating, configName));

    return this.newInstance(generating);
  }

  private ReferenceInvoker newInstance(CtClass target)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    byte[] bytes = target.toBytecode();
    Class<?> generated = LaunchController.getInstance().getRootLoader()
        .commonDefineClass(target.getName(), bytes, 0, bytes.length, null);

    return (ReferenceInvoker) generated.getDeclaredConstructor().newInstance();
  }

  private String buildPathCasts(GeneratingConfig config, CtMethod[] path) throws NotFoundException {
    StringBuilder builder = new StringBuilder();

    for (int i = path.length - 1; i >= 0; i--) {
      CtMethod method = path[i];

      CtClass returnType = method.getReturnType();
      CtClass castType = config.getGeneratedImplementation(returnType.getName(), returnType);
      builder.append("((").append(castType.getName()).append(')');
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
    String src = "public Object getValue(Object instance) {return " + valueSrc + ";}";

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

  private CtMethod generateReferenceSetter(
      String lastAccessor, CtClass declaring, String configName) throws CannotCompileException {
    // Field generated in ConfigMethodGenerationUtils.getReferenceField
    String fieldName = "configReference" + configName;

    String src = "public void setReference(Object instance, " + REFERENCE_CLASS + " reference) {"
        + lastAccessor
        + "."
        + fieldName
        + " = reference;"
        + "}";
    return CtNewMethod.make(src, declaring);
  }
}
