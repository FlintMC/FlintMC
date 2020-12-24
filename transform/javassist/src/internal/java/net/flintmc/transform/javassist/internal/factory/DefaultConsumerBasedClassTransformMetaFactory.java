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

package net.flintmc.transform.javassist.internal.factory;

import com.google.inject.Inject;
import javassist.CtClass;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.ConsumerBasedClassTransformMeta;
import net.flintmc.transform.javassist.internal.DefaultConsumerBasedClassTransformMeta;

import java.util.function.Consumer;

@Implement(ConsumerBasedClassTransformMeta.Factory.class)
public class DefaultConsumerBasedClassTransformMetaFactory implements ConsumerBasedClassTransformMeta.Factory {

  private final ClassTransformContext.Factory classTransformContextFactory;

  @Inject
  private DefaultConsumerBasedClassTransformMetaFactory(
          ClassTransformContext.Factory classTransformContextFactory) {
    this.classTransformContextFactory = classTransformContextFactory;
  }

  @Override
  public ConsumerBasedClassTransformMeta create(CtClass ctClass, int priority, Consumer<ClassTransformContext> execution) {
    return new DefaultConsumerBasedClassTransformMeta(
            this.classTransformContextFactory,
            ctClass,
            priority,
            execution
    );
  }
}
