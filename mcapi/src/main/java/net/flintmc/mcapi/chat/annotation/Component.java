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

package net.flintmc.mcapi.chat.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;

/**
 * This annotation can be used to declare a {@link ChatComponent} in an annotation.
 *
 * @see ComponentAnnotationSerializer
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

  /**
   * The text for the component. The {@link ComponentAnnotationSerializer} will use this either for
   * the {@link ComponentSerializer.Factory#legacy()} or, if {@link #translate()} is {@code true},
   * use it as a translationKey for a {@link TranslationComponent}.
   *
   * @return The text for the component
   */
  String value();

  /**
   * Whether the {@link #value()} should be translated or not.
   *
   * @return {@code true} if it should be translated, {@code false} otherwise
   */
  boolean translate() default false;
}
