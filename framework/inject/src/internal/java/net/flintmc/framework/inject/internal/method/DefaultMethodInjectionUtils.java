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

package net.flintmc.framework.inject.internal.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.method.MethodInjectionUtils;
import net.flintmc.framework.inject.method.MethodInjectorGenerationException;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.launcher.LaunchController;

/** {@inheritDoc} */
@Singleton
@Implement(MethodInjectionUtils.class)
public class DefaultMethodInjectionUtils implements MethodInjectionUtils {

  private static final String FACTORY_NAME =
      "net.flintmc.framework.inject.method.MethodInjector$Factory";

  private final ClassPool pool;
  private final InjectionUtils injectionUtils;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;
  private final AtomicInteger idCounter;

  @Inject
  private DefaultMethodInjectionUtils(
      InjectionUtils injectionUtils, InjectedFieldBuilder.Factory fieldBuilderFactory) {
    this.pool = ClassPool.getDefault();
    this.injectionUtils = injectionUtils;
    this.fieldBuilderFactory = fieldBuilderFactory;
    this.idCounter = new AtomicInteger();
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateInjector(CtClass target, CtMethod targetMethod, Class<?> ifc)
      throws CannotCompileException {
    return this.generateInjector(
        target, targetMethod.getDeclaringClass().getName(), targetMethod.getName(), ifc);
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateInjector(
      CtClass target, String targetClass, String methodName, Class<?> ifc)
      throws CannotCompileException {
    boolean moved = false;

    if (target == null || target.isInterface()) {
      // cannot add fields to an interface

      target =
          ClassPool.getDefault()
              .makeClass(
                  "MethodInjectionProvider_"
                      + this.idCounter.incrementAndGet()
                      + "_"
                      + UUID.randomUUID().toString().replace("-", ""));
      moved = true;
    }

    CtField injectedFactory =
        this.fieldBuilderFactory.create().target(target).inject(FACTORY_NAME).generate();

    String injectorName = this.injectionUtils.generateInjectedFieldName() + "_" + methodName;

    CtField injectorField =
        CtField.make(String.format("private static %s %s;", ifc.getName(), injectorName), target);
    target.addField(injectorField);

    String getterName = "getMethodInjector_" + injectorName;
    String generate =
        String.format(
            "if (%s == null) { %1$s = %s.generate(%s.getDefault().getMethod(\"%s\", \"%s\"), %s.class); }",
            injectorField.getDeclaringClass().getName() + "." + injectorName,
            injectedFactory.getName(),
            ClassPool.class.getName(),
            targetClass,
            methodName,
            ifc.getName());
    CtMethod getter =
        CtMethod.make(
            String.format(
                "public static %s %s() { %s return %s; }",
                ifc.getName(), getterName, generate, injectorName),
            target);

    try {
      getter.addCatch(
          String.format(
              "throw new %s(\"Method for generating a method injector not found: %s.%s\", $e);",
              IllegalStateException.class.getName(), targetClass, methodName),
          this.pool.get(NotFoundException.class.getName()));
    } catch (NotFoundException exception) {
      throw new MethodInjectorGenerationException(
          "Class " + NotFoundException.class.getName() + " not found");
    }
    target.addMethod(getter);

    if (moved) {
      try {
        CtResolver.defineClass(target);
      } catch (IOException exception) {
        throw new MethodInjectorGenerationException(
            String.format(
                "Failed to define holder for the method injector of %s.%s based on interface %s",
                targetClass, methodName, ifc.getName()),
            exception);
      }
    }

    return getter;
  }
}
