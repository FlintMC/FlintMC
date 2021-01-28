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

package net.flintmc.mcapi.v1_16_5.server.event;

import com.google.inject.Inject;
import java.util.ArrayList;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerListUpdateEvent;
import net.flintmc.mcapi.server.event.ServerListUpdateEvent.Type;
import net.minecraft.client.multiplayer.ServerData;

public class ModServerList extends ArrayList<ServerData> {

  private final EventBus eventBus;
  private final net.flintmc.mcapi.server.ServerData.Factory dataFactory;
  private final ServerAddress.Factory addressFactory;
  private final ServerListUpdateEvent.Factory eventFactory;
  private boolean enabled;

  @Inject
  private ModServerList(
      EventBus eventBus,
      net.flintmc.mcapi.server.ServerData.Factory dataFactory,
      ServerAddress.Factory addressFactory,
      ServerListUpdateEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.dataFactory = dataFactory;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
    this.enabled = true;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private ServerListUpdateEvent createEvent(int index, Type type, ServerData data) {
    net.flintmc.mcapi.server.ServerData flintData =
        this.dataFactory.create(
            data.serverName,
            this.addressFactory.parse(data.serverIP),
            net.flintmc.mcapi.server.ServerData.ResourceMode.valueOf(
                data.getResourceMode().name()));
    return this.eventFactory.create(index, flintData, type);
  }

  @Override
  public void add(int index, ServerData element) {
    if (!this.enabled) {
      super.add(index, element);
      return;
    }

    ServerListUpdateEvent event = this.createEvent(index, Type.ADD, element);
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return;
    }

    super.add(index, element);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  @Override
  public ServerData set(int index, ServerData element) {
    if (!this.enabled) {
      return super.set(index, element);
    }

    ServerListUpdateEvent event = this.createEvent(index, Type.UPDATE, element);
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return super.get(index);
    }

    ServerData previous = super.set(index, element);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    return previous;
  }

  @Override
  public boolean add(ServerData serverData) {
    if (!this.enabled) {
      return super.add(serverData);
    }

    if (super.contains(serverData)) {
      return false;
    }

    ServerListUpdateEvent event = this.createEvent(super.size(), Type.ADD, serverData);
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return false;
    }

    boolean success = super.add(serverData);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    return success;
  }

  @Override
  public boolean remove(Object o) {
    if (!this.enabled) {
      return super.remove(o);
    }

    if (!(o instanceof ServerData)) {
      return false;
    }
    int index = super.indexOf(o);
    if (index == -1) {
      return false;
    }
    ServerListUpdateEvent event = this.createEvent(index, Type.REMOVE, (ServerData) o);
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return false;
    }

    boolean success = super.remove(o);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    return success;
  }

  @Override
  public ServerData remove(int index) {
    if (!this.enabled) {
      return super.remove(index);
    }

    ServerListUpdateEvent event = this.createEvent(index, Type.REMOVE, super.get(index));
    if (this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled()) {
      return null;
    }

    ServerData removed = super.remove(index);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    return removed;
  }
}
