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

package net.flintmc.render.gui.internal.windowing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.flintmc.render.gui.windowing.MinecraftWindow;

@Singleton
public class DefaultWindowManagerHandler {

  private final DefaultWindowManager windowManager;

  @Inject
  private DefaultWindowManagerHandler(DefaultWindowManager windowManager) {
    this.windowManager = windowManager;
  }

  /**
   * Registers the minecraft window after it has been initialized with OpenGL.
   */
  @Subscribe(phase = Subscribe.Phase.POST)
  public void postOpenGLInitialize(MinecraftWindow window, OpenGLInitializeEvent event) {
    this.windowManager.minecraftWindow = window;
    this.windowManager.registerWindow((InternalWindow) window);
  }
}
