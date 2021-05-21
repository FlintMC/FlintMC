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

package net.flintmc.render.gui.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.render.gui.event.ScreenChangedEvent;
import net.flintmc.render.gui.event.ScreenRenderEvent;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.screen.ScreenNameMapper;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookFilter;
import net.flintmc.transform.hook.HookFilters;
import net.flintmc.transform.hook.HookResult;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Named;

/**
 * 1.15.2 Implementation of the gui interceptor
 */
@Singleton
public class VersionedGuiInterceptor {

  private final ClassMappingProvider mappingProvider;
  private final DefaultWindowManager windowManager;
  private final ScreenNameMapper nameMapper;
  private final InjectedFieldBuilder.Factory fieldBuilder;

  @Inject
  private VersionedGuiInterceptor(
      ClassMappingProvider mappingProvider,
      DefaultWindowManager windowManager,
      ScreenNameMapper nameMapper,
      InjectedFieldBuilder.Factory fieldBuilder) {
    this.mappingProvider = mappingProvider;
    this.windowManager = windowManager;
    this.nameMapper = nameMapper;
    this.fieldBuilder = fieldBuilder;
  }

  @PostSubscribe
  public void hookMinecraftWindowRender(ScreenRenderEvent event) {
    this.windowManager.renderMinecraftWindow();
  }

  @HookFilter(value = HookFilters.SUBCLASS_OF, type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  @Hook(
      methodName = "render",
      parameters = {
          @Type(reference = int.class),
          @Type(reference = int.class),
          @Type(reference = float.class)},
      executionTime = Hook.ExecutionTime.BEFORE)
  public HookResult hookScreenRender(@Named("instance") Object instance) {
    if (this.windowManager.isMinecraftWindowRenderedIntrusively(instance.getClass().getName())) {
      return HookResult.BREAK;
    }
    return HookResult.CONTINUE;
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookScreenChanged() {
    ScreenName screenName = this.nameMapper.fromObject(Minecraft.getInstance().currentScreen);
    this.windowManager.fireEvent(-1, window -> new ScreenChangedEvent(window, screenName));
  }
}
