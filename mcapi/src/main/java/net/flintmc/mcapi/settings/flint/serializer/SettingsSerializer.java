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

package net.flintmc.mcapi.settings.flint.serializer;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.metaprogramming.DetectableAnnotation;

/**
 * Marks a {@link SettingsSerializationHandler} to be registered in the {@link
 * JsonSettingsSerializer}. The annotated class has to implement {@link
 * SettingsSerializationHandler}.
 *
 * @see SettingsSerializationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface SettingsSerializer {

  /**
   * Retrieves the annotation that can be serialized into a json element by the annotated
   * serialization handler. For example {@link DisplayName}.
   *
   * @return The annotation that can be serialized
   */
  Class<? extends Annotation> value();
}
