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
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.internal.DefaultClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
@Implement(ClassTransformContext.Factory.class)
public class DefaultClassTransformContextFactory implements ClassTransformContext.Factory {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private DefaultClassTransformContextFactory(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @Override
  public ClassTransformContext create(CtClass ctClass) {
    return new DefaultClassTransformContext(this.classMappingProvider, ctClass);
  }
}
