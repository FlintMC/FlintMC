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

package net.flintmc.mcapi.internal.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.debug.MinecraftDebugger;
import net.flintmc.mcapi.internal.event.DefaultDebugKeyHookEvent;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.render.gui.input.Key;
import net.flintmc.util.commons.Pair;

/**
 * {@inheritDoc}
 */
@Implement(MinecraftDebugger.class)
@Singleton
public class DefaultMinecraftDebugger implements MinecraftDebugger {

  private final Map<Key, Pair<ChatComponent, BooleanSupplier>> registeredKeyBindings;
  private final Map<Key, ChatComponent> minecraftProvided;

  private final TextComponentBuilder.Factory textComponentBuilderFactory;
  private final TranslationComponentBuilder.Factory translationComponentBuilderFactory;
  private final ClientPlayer player;
  private final EventBus eventBus;

  @Inject
  private DefaultMinecraftDebugger(
      TextComponentBuilder.Factory textComponentBuilderFactory,
      TranslationComponentBuilder.Factory translationComponentBuilderFactory,
      ClientPlayer player,
      EventBus eventBus
  ) {
    this.textComponentBuilderFactory = textComponentBuilderFactory;
    this.translationComponentBuilderFactory = translationComponentBuilderFactory;
    this.player = player;
    this.eventBus = eventBus;

    this.registeredKeyBindings = new HashMap<>();
    this.minecraftProvided = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerDebugKeybinding(Key key, ChatComponent description,
      BooleanSupplier runnable) {
    registeredKeyBindings.put(key, new Pair<>(description, runnable));
  }

  // Internal API for now
  public void registerInternalDebugKeybindings(Map<Key, String> keybindings) {
    keybindings.forEach((key, translation) -> minecraftProvided.put(key,
        translationComponentBuilderFactory.newBuilder().translationKey(translation).build()));
  }

  public boolean handleDebugKey(Key key) {
    if (minecraftProvided.containsKey(key) || registeredKeyBindings.containsKey(key)) {
      eventBus.fireEvent(new DefaultDebugKeyHookEvent(key), Phase.PRE);
    }

    Pair<ChatComponent, BooleanSupplier> binding = registeredKeyBindings.get(key);

    if (binding == null) {
      return false;
    }

    return binding.second().getAsBoolean();
  }

  // Internal API for now
  public boolean displayHelp() {
    // TODO: Translate
    player.sendMessage(
        textComponentBuilderFactory.newBuilder().text("Minecraft provided debug keybindings:")
            .build(), null);
    minecraftProvided.forEach((key, component) -> player.sendMessage(component, null));

    // TODO: Translate
    player.sendMessage(
        textComponentBuilderFactory.newBuilder().text("Flint provided debug keybindings:").build(),
        null);
    registeredKeyBindings.forEach((key, data) -> player.sendMessage(data.first(), null));
    return true;
  }
}
