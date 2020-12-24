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

package net.flintmc.transform.javassist.internal;

import com.google.inject.Inject;
import javassist.CtClass;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.ConsumerBasedClassTransformMeta;

import java.util.function.Consumer;

public class DefaultConsumerBasedClassTransformMeta implements ConsumerBasedClassTransformMeta {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final CtClass ctClass;
  private final int priority;
  private final Consumer<ClassTransformContext> execution;

  @Inject
  public DefaultConsumerBasedClassTransformMeta(
          ClassTransformContext.Factory classTransformContextFactory,
          CtClass ctClass,
          int priority,
          Consumer<ClassTransformContext> execution) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.ctClass = ctClass;
    this.priority = priority;
    this.execution = execution;
  }

  @Override
  public void execute(CtClass ctClass) throws ClassTransformException {
    this.execution.accept(this.classTransformContextFactory.create(ctClass));
  }

  @Override
  public boolean matches(CtClass ctClass) {
    return this.ctClass.getName().equals(ctClass.getName());
  }

  @Override
  public int getPriority() {
    return this.priority;
  }
}
