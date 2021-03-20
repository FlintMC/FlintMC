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
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.MethodBasedClassTransformMeta;
import net.flintmc.transform.javassist.internal.DefaultMethodBasedClassTransformMeta;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(MethodBasedClassTransformMeta.Factory.class)
public class DefaultMethodBasedClassTransformMetaFactory implements
    MethodBasedClassTransformMeta.Factory {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final ClassMappingProvider classMappingProvider;
  private final Logger logger;

  @Inject
  private DefaultMethodBasedClassTransformMetaFactory(
      DefaultClassTransformContextFactory classTransformContextFactory,
      ClassMappingProvider classMappingProvider
  ) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.classMappingProvider = classMappingProvider;
    this.logger = null;
  }

  @Override
  public MethodBasedClassTransformMeta create(AnnotationMeta<ClassTransform> annotationMeta) {
    return new DefaultMethodBasedClassTransformMeta(
        this.classTransformContextFactory,
        this.classMappingProvider,
        this.logger,
        annotationMeta
    );
  }
}
