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

package net.flintmc.mcapi.internal.settings.flint.serializer;

import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

import java.lang.annotation.Annotation;

public class RegisteredSettingsSerializer<A extends Annotation> {

  private final Class<A> annotationType;
  private final SettingsSerializationHandler<A> handler;

  public RegisteredSettingsSerializer(
      Class<A> annotationType, SettingsSerializationHandler<A> handler) {
    this.annotationType = annotationType;
    this.handler = handler;
  }

  public Class<A> getAnnotationType() {
    return this.annotationType;
  }

  public SettingsSerializationHandler<A> getHandler() {
    return this.handler;
  }
}
