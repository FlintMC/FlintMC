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

package net.flintmc.mcapi.world.stats.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.mcapi.world.stats.WorldStats;
import net.flintmc.mcapi.world.stats.WorldStatsProvider;

/**
 * This event will be fired when the server sends an update for the {@link WorldStats}, this will
 * normally be done after the client sent a request which for example can be when {@link
 * WorldStatsProvider#requestStatsUpdate()} has been invoked.
 * <p>
 * It will be fired in both the {@link Phase#PRE} and {@link Phase#POST} phases.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface PlayerStatsUpdateEvent extends Event {

}
