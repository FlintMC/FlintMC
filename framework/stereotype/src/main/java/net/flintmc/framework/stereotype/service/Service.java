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

package net.flintmc.framework.stereotype.service;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

/**
 * A service can be used to discover {@link DetectableAnnotation}s. {@link Service} marks a class
 * which implements {@link ServiceHandler}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Service {

  /** @return the target Identifier type */
  Class<? extends Annotation>[] value();

  /** @return the loading priority. lower priorities are loaded first */
  int priority() default 0;

  State state() default State.POST_INIT;

  enum State {
    PRE_INIT,
    AFTER_IMPLEMENT,
    POST_INIT
  }
}
