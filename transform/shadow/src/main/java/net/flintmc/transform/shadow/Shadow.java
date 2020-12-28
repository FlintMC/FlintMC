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

package net.flintmc.transform.shadow;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a shadow interface for a target class.
 *
 * <p>A shadow interface is used to access fields and methods in other already compiled classes that
 * cant be accessed without reflections or similar techniques.
 *
 * <p>This is done by bytecode manipulating the target class and adding the - by this annotation -
 * marked class to its interface list. So any instance of the target class can be casted to the
 * interface and the generated or modified methods can be accessed without compile errors.
 *
 * @see FieldGetter
 * @see FieldSetter
 * @see MethodProxy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation(
    metaData = {FieldCreate.class, FieldGetter.class, FieldSetter.class, MethodProxy.class})
public @interface Shadow {
  /** @return the target class name that should be modified */
  String value();
}
