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

package net.flintmc.framework.inject.assisted;

import com.google.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;
import net.flintmc.metaprogramming.DetectableAnnotation;

/**
 * When used in tandem with {@link AssistedFactoryModuleBuilder}, constructor annotated with {@link
 * AssistedInject} indicate that multiple constructors can be injected, each with different
 * parameters. {@link AssistedInject} annotations should not be mixed {@link Inject} annotations.
 * The assisted parameters must exactly match one corresponding factory method within the factory
 * interface, but the parameters do not need to be in the same order. Constructors annotated with
 * {@link AssistedInject} <b>are</b> created by <b>Guice</b> and receive all the benefits (such as
 * AOP).
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface AssistedInject {}
