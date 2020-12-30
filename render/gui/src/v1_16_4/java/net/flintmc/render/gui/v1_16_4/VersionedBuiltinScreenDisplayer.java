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

package net.flintmc.render.gui.v1_16_4;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.screen.BuiltinScreenDisplayer;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.v1_16_4.lazy.VersionedBuiltinScreenDisplayInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

/** 1.16.4 Implementation of the {@link BuiltinScreenDisplayer} */
@Singleton
@Implement(value = BuiltinScreenDisplayer.class, version = "1.16.4")
public class VersionedBuiltinScreenDisplayer implements BuiltinScreenDisplayer {
  private final Map<ScreenName, Consumer<Object[]>> supportedScreens;

  private boolean initialized;

  @Inject
  private VersionedBuiltinScreenDisplayer() {
    this.supportedScreens = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public boolean supports(ScreenName screenName) {
    if (!initialized) {
      VersionedBuiltinScreenDisplayInit.init(supportedScreens);
      initialized = true;
    }

    return supportedScreens.containsKey(screenName);
  }

  /** {@inheritDoc} */
  @Override
  public void display(ScreenName screenName, Object... args) {
    if (!supports(screenName)) {
      throw new UnsupportedOperationException(
          "This displayer does not support the screen" + screenName);
    }

    supportedScreens.get(screenName).accept(args);
  }

  @Override
  public ScreenName getOpenScreen() {
    Screen screen = Minecraft.getInstance().currentScreen;
    return screen != null ? ScreenName.unknown(screen.getClass().getName()) : null;
  }
}
