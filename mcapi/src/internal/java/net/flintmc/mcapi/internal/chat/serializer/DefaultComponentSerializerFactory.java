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

package net.flintmc.mcapi.internal.chat.serializer;

import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.DefaultGsonComponentSerializer;
import org.apache.logging.log4j.Logger;

@Singleton
public class DefaultComponentSerializerFactory implements ComponentSerializer.Factory {

  private final ComponentSerializer legacy;
  private final ComponentSerializer plain;
  private final GsonComponentSerializer gson;

  public DefaultComponentSerializerFactory(
      Logger logger, ComponentBuilder.Factory componentFactory, boolean legacyHover) {
    this.legacy =
        new PlainComponentSerializer(logger, true); // plain serializer with all colors/formatting
    this.plain =
        new PlainComponentSerializer(
            logger, false); // plain serializer without any colors/formatting
    this.gson =
        new DefaultGsonComponentSerializer(
            logger, componentFactory, legacyHover); // in 1.16 the hoverEvent has completely changed
  }

  @Override
  public ComponentSerializer legacy() {
    return this.legacy;
  }

  @Override
  public ComponentSerializer plain() {
    return this.plain;
  }

  @Override
  public GsonComponentSerializer gson() {
    return this.gson;
  }
}
