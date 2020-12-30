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

package net.flintmc.render.gui.v1_16_4.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedOpenGLInitializeEventInjector {

  private final EventBus eventBus;

  private final OpenGLInitializeEvent event;

  @Inject
  public VersionedOpenGLInitializeEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new OpenGLInitializeEvent() {};
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = "net.minecraft.client.Minecraft",
      methodName = "isMultiplayerEnabled",
      version = "1.16.4")
  public void preInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.MainWindow",
      methodName = "setLogOnGlError",
      version = "1.16.4")
  public void postInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.POST);
  }
}
