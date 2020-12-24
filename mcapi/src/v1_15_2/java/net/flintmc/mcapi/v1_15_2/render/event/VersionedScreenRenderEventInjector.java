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

package net.flintmc.mcapi.v1_15_2.render.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.render.event.ScreenRenderEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class VersionedScreenRenderEventInjector {

  private final EventBus eventBus;
  private final ScreenRenderEvent event;

  @Inject
  private VersionedScreenRenderEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new ScreenRenderEvent() {};
  }

  @Hook(
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "updateCameraAndRender",
      parameters = {
        @Type(reference = float.class),
        @Type(reference = long.class),
        @Type(reference = boolean.class)
      },
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void renderScreen(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.event, executionTime);
  }
}
