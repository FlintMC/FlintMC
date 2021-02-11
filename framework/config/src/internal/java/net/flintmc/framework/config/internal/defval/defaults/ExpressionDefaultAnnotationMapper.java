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

package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.defval.annotation.DefaultExpression;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.launcher.LaunchController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@DefaultAnnotationMapper(DefaultExpression.class)
public class ExpressionDefaultAnnotationMapper
    implements DefaultAnnotationMapperHandler<DefaultExpression> {

  private final ClassPool pool;
  private final Map<DefaultExpression, ExpressionEvaluator> cachedEvaluators;

  private final CtClass evaluatorInterface;

  private final Random random;
  private final AtomicInteger idCounter;

  @Inject
  private ExpressionDefaultAnnotationMapper(ClassPool pool) throws NotFoundException {
    this.pool = pool;
    this.evaluatorInterface = pool.get(ExpressionEvaluator.class.getName());
    this.cachedEvaluators = new HashMap<>();

    this.random = new Random();
    this.idCounter = new AtomicInteger();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultExpression annotation) {
    return this.cachedEvaluators.computeIfAbsent(annotation, k -> this.generate(k.value())).eval();
  }

  private ExpressionEvaluator generate(String expression) {
    CtClass generating = this.pool.makeClass(
        "ExpressionEvaluator" + this.idCounter.incrementAndGet() + "_" + this.random
            .nextInt(Integer.MAX_VALUE));

    generating.addInterface(this.evaluatorInterface);

    try {
      generating.addMethod(
          CtNewMethod.make("public Object eval() { return " + expression + "; }", generating));
    } catch (CannotCompileException exception) {
      throw new IllegalStateException(
          "Failed to compile expression '" + expression + "'", exception);
    }

    try {
      byte[] bytes = generating.toBytecode();
      Class<?> defined = LaunchController.getInstance().getRootLoader()
          .commonDefineClass(generating.getName(), bytes, 0, bytes.length, null);
      return (ExpressionEvaluator) defined.getDeclaredConstructor().newInstance();
    } catch (IOException | ReflectiveOperationException | CannotCompileException exception) {
      throw new IllegalStateException(
          "Failed to create an instance of expression evaluator for expression '" + expression
              + "'", exception);
    }
  }

  public static interface ExpressionEvaluator {

    Object eval();
  }
}
