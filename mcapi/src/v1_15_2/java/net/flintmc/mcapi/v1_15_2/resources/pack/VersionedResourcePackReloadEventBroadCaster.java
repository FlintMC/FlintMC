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

package net.flintmc.mcapi.v1_15_2.resources.pack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.resources.pack.ResourcePackReloadEvent;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;

@Singleton
public class VersionedResourcePackReloadEventBroadCaster {

  private final EventBus eventBus;
  private final ResourcePackReloadEvent resourcePackReloadEvent;

  @Inject
  private VersionedResourcePackReloadEventBroadCaster(
      EventBus eventBus, ResourcePackReloadEvent resourcePackReloadEvent) {
    this.eventBus = eventBus;
    this.resourcePackReloadEvent = resourcePackReloadEvent;
  }

  @PostSubscribe
  public void init(OpenGLInitializeEvent event) {
    // Install a hook on the minecraft resource manager
    ((SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager())
        .addReloadListener(
            (IResourceManagerReloadListener)
                iResourceManager ->
                    this.eventBus.fireEvent(this.resourcePackReloadEvent, Subscribe.Phase.POST));
  }
}
