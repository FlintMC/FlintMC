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

package net.flintmc.mcapi.v1_15_2.chat.component.event;

import com.google.inject.Singleton;
import java.io.File;
import net.flintmc.mcapi.chat.component.event.ClickActionExecutor;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.chat.component.event.ClickEventAction;
import net.minecraft.util.Util;

@Singleton
@ClickEventAction(value = Action.OPEN_FILE, priority = 0)
public class VersionedOpenFileActionExecutor implements ClickActionExecutor {

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeEvent(ClickEvent event) {
    Util.getOSType().openFile(new File(event.getValue()));
  }
}
