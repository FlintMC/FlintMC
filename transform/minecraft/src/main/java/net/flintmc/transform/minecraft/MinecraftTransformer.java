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

package net.flintmc.transform.minecraft;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.metaprogramming.DetectableAnnotation;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link LateInjectedTransformer} to be registered and detected automatically by a {@link
 * ServiceHandler}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface MinecraftTransformer {

  /**
   * Defines the priority order in which the {@link LateInjectedTransformer} will handle class
   * transformations.
   *
   * <p>Lowest priority will be called first.
   *
   * @return the priority for class transformations
   */
  int priority() default 0;

  /**
   * Defines whether the transformer class requires the implementations to be bound or not. If this
   * is {@code false}, no interfaces with their implementations marked with {@link Implement} can be
   * used.
   * <p>
   * If classes that are discovered by the {@link Implement} annotation should be transformed, this
   * has to be {@code false} because otherwise they would be loaded before this transformer is
   * registered.
   *
   * @return {@code true} if implementations are required, {@code false} if they are not
   */
  boolean implementations() default true;

}
