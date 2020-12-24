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

package net.flintmc.processing;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.TypeElement;

/**
 * Service interface for annotation processors controlled by the {@link FlintAnnotationProcessor}.
 * To hook into this processor, use a java service file, it will then be loaded by the default
 * {@link java.util.ServiceLoader}{@literal <}{@link Processor}{@literal >}.
 */
public interface Processor {
  /**
   * Called by the {@link ProcessorState} of the current {@link FlintAnnotationProcessor} for every
   * annotation found.
   *
   * @param typeElement The annotation element
   */
  void accept(TypeElement typeElement);

  /**
   * Called by the {@link ProcessorState} of the current {@link FlintAnnotationProcessor} in the
   * final round to allow generation of code.
   *
   * @return The method that should be added to the autoload class
   */
  MethodSpec.Builder createMethod();

  /**
   * Called by the {@link ProcessorState} of the current {@link FlintAnnotationProcessor} in the
   * final round to determine, which superclass should be added to the class generated for the
   * autoload method.
   *
   * @return The name of the class to add as a super class
   */
  ClassName getGeneratedClassSuperClass();

  /**
   * Called by the {@link ProcessorState} of the current {@link FlintAnnotationProcessor} in the
   * final round to finalize the code generation of the given method.
   *
   * @param targetMethod The method to finalize, will always be the method returned by {@link
   *     #createMethod()}
   */
  void finish(MethodSpec.Builder targetMethod);
}
