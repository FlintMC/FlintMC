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
import com.google.inject.name.Named;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.MethodBasedClassTransformMeta;
import net.flintmc.transform.javassist.internal.DefaultMethodBasedClassTransformMeta;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Implement(MethodBasedClassTransformMeta.Factory.class)
public class DefaultMethodBasedClassTransformMetaFactory implements MethodBasedClassTransformMeta.Factory {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final ClassMappingProvider classMappingProvider;
  private final Logger logger;
  private final Map<String, String> launchArguments;

  @Inject
  private DefaultMethodBasedClassTransformMetaFactory(
          ClassTransformContext.Factory classTransformContextFactory,
          ClassMappingProvider classMappingProvider,
          Logger logger,
          @Named("launchArguments") Map launchArguments) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.classMappingProvider = classMappingProvider;
    this.logger = logger;
    this.launchArguments = launchArguments;
  }

  @Override
  public MethodBasedClassTransformMeta create(AnnotationMeta annotationMeta) {
    return new DefaultMethodBasedClassTransformMeta(
            this.classTransformContextFactory,
            this.classMappingProvider,
            this.logger,
            annotationMeta,
            this.launchArguments
    );
  }
}
