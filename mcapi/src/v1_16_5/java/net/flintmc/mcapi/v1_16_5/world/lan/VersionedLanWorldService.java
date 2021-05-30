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

package net.flintmc.mcapi.v1_16_5.world.lan;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder.Factory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.world.lan.LanWorldOpenEvent;
import net.flintmc.mcapi.world.lan.LanWorldOptions;
import net.flintmc.mcapi.world.lan.LanWorldService;
import net.minecraft.client.Minecraft;
import net.minecraft.util.HTTPUtil;
import net.minecraft.world.GameType;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(LanWorldService.class)
public class VersionedLanWorldService implements LanWorldService {

  private final Logger logger;

  private final EntityFoundationMapper entityMapper;
  private final ChatController chatController;
  private final Factory translationComponentFactory;

  private final EventBus eventBus;
  private final LanWorldOpenEvent.Factory openEventFactory;

  @Inject
  private VersionedLanWorldService(
      @InjectLogger Logger logger,
      EntityFoundationMapper entityMapper,
      ChatController chatController,
      Factory translationComponentFactory,
      EventBus eventBus,
      LanWorldOpenEvent.Factory openEventFactory) {
    this.logger = logger;
    this.entityMapper = entityMapper;
    this.chatController = chatController;
    this.translationComponentFactory = translationComponentFactory;
    this.eventBus = eventBus;
    this.openEventFactory = openEventFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAvailable() {
    return Minecraft.getInstance().isSingleplayer()
        && !Minecraft.getInstance().getIntegratedServer().getPublic();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean openLanWorld(LanWorldOptions options) {
    if (!this.isAvailable()) {
      throw new IllegalStateException("Cannot open a LAN world while it is not available");
    }

    int port = HTTPUtil.getSuitableLanPort();
    LanWorldOpenEvent preEvent = this.openEventFactory.create(false, port, options);
    if (this.eventBus.fireEvent(preEvent, Phase.PRE).isCancelled()) {
      this.logger.debug("Not opening lan world because the LanWorldOpenEvent has been cancelled");
      return false;
    }

    options = preEvent.getOptions();
    port = preEvent.getPort();

    this.logger.debug("Opening lan world on {}", port);

    GameType type = (GameType) this.entityMapper.toMinecraftGameType(options.mode());

    boolean success = Minecraft.getInstance().getIntegratedServer()
        .shareToLAN(type, options.allowCheats(), port);

    if (options.showInfoMessage()) {
      // Display whether the server is started or not
      ChatComponent infoMessage = this.getInfoMessage(success, port);
      this.chatController.displayChatMessage(ChatLocation.SYSTEM, infoMessage);
    }

    // Update the window title
    Minecraft.getInstance().setDefaultMinecraftTitle();

    this.eventBus.fireEvent(this.openEventFactory.create(success, port, options), Phase.POST);

    return success;
  }

  private ChatComponent getInfoMessage(boolean success, int port) {
    if (success) {
      return this.translationComponentFactory.newBuilder()
          .translationKey("commands.publish.started")
          .appendArgument(String.valueOf(port))
          .build();
    } else {
      return this.translationComponentFactory.newBuilder()
          .translationKey("commands.publish.failed")
          .build();
    }
  }
}
