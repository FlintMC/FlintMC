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

package net.flintmc.mcapi.internal.chat.serializer.gson;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent.Action;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.LegacyHoverEventSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.ModernHoverEventSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.content.HoverEntitySerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.content.HoverItemSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.content.HoverTextSerializer;
import net.flintmc.mcapi.version.VersionHelper;
import org.apache.logging.log4j.Logger;

@Singleton
public class DefaultGsonComponentSerializer implements GsonComponentSerializer {

  private final Gson gson;
  private final ComponentBuilder.Factory componentFactory;

  private final Map<Action, HoverContentSerializer> hoverContentSerializers = new HashMap<>();

  @Inject
  private DefaultGsonComponentSerializer(
      @InjectLogger Logger logger,
      GsonChatComponentSerializer componentSerializer,
      ComponentBuilder.Factory componentFactory,
      HoverEvent.Factory eventFactory,
      VersionHelper versionHelper,
      HoverTextSerializer textSerializer,
      HoverEntitySerializer entitySerializer,
      HoverItemSerializer itemSerializer) {
    this.componentFactory = componentFactory;

    this.gson =
        new GsonBuilder()
            .registerTypeHierarchyAdapter(ChatComponent.class, componentSerializer)
            .registerTypeHierarchyAdapter(
                HoverEvent.class,
                versionHelper.getMinor() < 16
                    ? new LegacyHoverEventSerializer(logger, componentFactory, this, eventFactory)
                    : new ModernHoverEventSerializer(logger, this, componentFactory, eventFactory))
            .create();

    this.registerHoverContentSerializer(Action.SHOW_TEXT, textSerializer);
    this.registerHoverContentSerializer(Action.SHOW_ENTITY, entitySerializer);
    this.registerHoverContentSerializer(Action.SHOW_ACHIEVEMENT, textSerializer);
    this.registerHoverContentSerializer(Action.SHOW_ITEM, itemSerializer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(ChatComponent component) {
    return this.gson.toJson(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent deserialize(String serialized) {
    try {
      return this.gson.fromJson(serialized, ChatComponent.class);
    } catch (JsonSyntaxException exception) {
      throw new ComponentDeserializationException("Invalid json", exception);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Gson getGson() {
    return this.gson;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent deserializeHoverContent(ChatComponent component, Action action) {
    return this.getHoverContentSerializer(action)
        .deserialize(component, this.componentFactory, this.gson);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent serializeHoverContent(HoverContent content) {
    return this.getHoverContentSerializer(content.getAction())
        .serialize(content, this.componentFactory, this.gson);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerHoverContentSerializer(Action action, HoverContentSerializer serializer) {
    Preconditions.checkArgument(
        !this.hoverContentSerializers.containsKey(action),
        "A serializer for the action %s is already registered",
        action);

    this.hoverContentSerializers.put(action, serializer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContentSerializer getHoverContentSerializer(Action action) {
    HoverContentSerializer serializer = this.hoverContentSerializers.get(action);
    if (serializer == null) {
      throw new UnsupportedOperationException("No serializer for the action " + action + " found");
    }
    return serializer;
  }
}
