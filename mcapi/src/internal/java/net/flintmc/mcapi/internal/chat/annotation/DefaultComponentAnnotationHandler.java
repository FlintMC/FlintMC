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

package net.flintmc.mcapi.internal.chat.annotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.annotation.DefaultComponent;

@Singleton
@DefaultAnnotationMapper(DefaultComponent.class)
public class DefaultComponentAnnotationHandler
    implements DefaultAnnotationMapperHandler<DefaultComponent> {

  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  private DefaultComponentAnnotationHandler(ComponentAnnotationSerializer annotationSerializer) {
    this.annotationSerializer = annotationSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultComponent annotation) {
    return this.annotationSerializer.deserialize(annotation.value());
  }
}
