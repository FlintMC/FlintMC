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

package net.flintmc.framework.config.annotation.implemented;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.processing.autoload.DetectableAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The counterpart to {@link ImplementedConfig}. Use it on the implementations of interfaces
 * annotated with {@link ImplementedConfig} to mark them as their implementation.
 *
 * @see Config
 * @see ImplementedConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface ConfigImplementation {

  /**
   * The interface implemented by this class. This means, the class also needs to implement the
   * given interface and have the {@link ImplementedConfig} annotation.
   *
   * @return The interface implemented by this class
   */
  Class<?> value();

  /**
   * The minecraft version this {@code @Implement} applies to. If the version does not match, the
   * implementation is not bound.
   *
   * @return The version this {@code @Implement} applies to
   */
  String version() default "";

}
