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

package net.flintmc.mcapi.v1_16_5.chat.component.event;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.chat.component.event.ClickActionExecutor;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.chat.component.event.ClickEventAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;

@Singleton
@ClickEventAction(value = Action.OPEN_URL, priority = 0)
public class VersionedOpenUrlActionExecutor implements ClickActionExecutor {

  private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");

  private final Logger logger;

  @Inject
  private VersionedOpenUrlActionExecutor(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeEvent(ClickEvent event) {
    try {
      String value = event.getValue();

      URI uri = new URI(value);
      String scheme = uri.getScheme();
      if (scheme == null) {
        throw new URISyntaxException(value, "Missing protocol");
      }

      if (!ALLOWED_PROTOCOLS.contains(scheme.toLowerCase())) {
        throw new URISyntaxException(value, "Unsupported URI scheme: " + scheme);
      }

      if (Minecraft.getInstance().gameSettings.chatLinksPrompt) {
        Screen previousScreen = Minecraft.getInstance().currentScreen;

        Minecraft.getInstance().displayGuiScreen(new ConfirmOpenLinkScreen(accepted -> {
          if (accepted) {
            Util.getOSType().openURI(uri);
          }

          Minecraft.getInstance().displayGuiScreen(previousScreen);
        }, value, false));
        return;
      }

      Util.getOSType().openURI(uri);
    } catch (URISyntaxException e) {
      this.logger.error("Invalid URI: {}", event.getValue(), e);
    }
  }
}
