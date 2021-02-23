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

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultNumber;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultNumber.class)
public class NumberDefaultAnnotationMapper
    implements DefaultAnnotationMapperHandler<DefaultNumber> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultNumber annotation) {
    return annotation.value();
  }
}
