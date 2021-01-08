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

package net.flintmc.mcapi.v1_16_4.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.event.TabHeaderFooterUpdateEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;

@Singleton
public class VersionedTabHeaderFooterEventInjector {

  private final EventBus eventBus;
  private final MinecraftComponentMapper componentMapper;
  private final TabHeaderFooterUpdateEvent.Factory eventFactory;

  @Inject
  private VersionedTabHeaderFooterEventInjector(
      EventBus eventBus,
      MinecraftComponentMapper componentMapper,
      TabHeaderFooterUpdateEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.componentMapper = componentMapper;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "setHeader",
      parameters = @Type(typeName = "net.minecraft.util.text.ITextComponent"),
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER}, version = "1.16.4")
  public HookResult handleHeader(@Named("args") Object[] args, ExecutionTime executionTime) {
    Object newHeader = args[0];

    return this.fire(newHeader, TabHeaderFooterUpdateEvent.Type.HEADER, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "setFooter",
      parameters = @Type(typeName = "net.minecraft.util.text.ITextComponent"),
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      version = "1.16.4")
  public HookResult handleFooter(@Named("args") Object[] args, ExecutionTime executionTime) {
    Object newFooter = args[0];

    return this.fire(newFooter, TabHeaderFooterUpdateEvent.Type.FOOTER, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "resetFooterHeader",
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      version = "1.16.4")
  public HookResult handleReset(ExecutionTime executionTime) {
    TabHeaderFooterUpdateEvent headerEvent =
        this.eventFactory.create(null, TabHeaderFooterUpdateEvent.Type.HEADER);
    TabHeaderFooterUpdateEvent footerEvent =
        this.eventFactory.create(null, TabHeaderFooterUpdateEvent.Type.HEADER);

    boolean cancelled =
        this.eventBus.fireEvent(headerEvent, executionTime).isCancelled()
            || this.eventBus.fireEvent(footerEvent, executionTime).isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }

  private HookResult fire(
      Object newValue, TabHeaderFooterUpdateEvent.Type type, ExecutionTime executionTime) {

    TabHeaderFooterUpdateEvent event =
        this.eventFactory.create(
            newValue == null ? null : this.componentMapper.fromMinecraft(newValue), type);
    boolean cancelled = this.eventBus.fireEvent(event, executionTime).isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }
}
