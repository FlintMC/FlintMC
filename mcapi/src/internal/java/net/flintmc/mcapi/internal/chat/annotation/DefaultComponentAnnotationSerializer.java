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

package net.flintmc.mcapi.internal.chat.annotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;

@Singleton
@Implement(ComponentAnnotationSerializer.class)
public class DefaultComponentAnnotationSerializer implements ComponentAnnotationSerializer {

  private final ComponentBuilder.Factory builderFactory;
  private final ComponentSerializer.Factory serializerFactory;

  @Inject
  private DefaultComponentAnnotationSerializer(
      ComponentBuilder.Factory builderFactory, ComponentSerializer.Factory serializerFactory) {
    this.builderFactory = builderFactory;
    this.serializerFactory = serializerFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent deserialize(Component component) {
    if (component.translate()) {
      // the value will be used as a translation key
      return this.builderFactory.translation().translationKey(component.value()).build();
    }

    // the value will be parsed as a legacy text like "§ctest"
    return this.serializerFactory.legacy().deserialize(component.value());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent deserialize(Component[] components) {
    TextComponentBuilder builder = this.builderFactory.text().text("");

    for (Component component : components) {
      builder.append(this.deserialize(component));
    }

    return builder.build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent deserialize(Component[] components, ChatComponent def) {
    if (components.length == 0) {
      return def;
    }

    return this.deserialize(components);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent deserialize(Component[] components, String def) {
    if (components.length == 0) {
      return this.serializerFactory.legacy().deserialize(def);
    }

    return this.deserialize(components);
  }
}
