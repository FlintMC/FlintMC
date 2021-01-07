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

package net.flintmc.render.gui.webgui;

import java.util.Collection;
import net.flintmc.util.commons.Pair;

/**
 * Holds a collection of discovered web filesystem implementations.
 */
public interface WebFileSystemService {

  /**
   * Gets a collection of web filesystem implementations and their protocol name. Creates the
   * instances via the injection holder if they haven't been created yet.
   *
   * @return a {@link Pair} with the first element being the filesystem instance and the second
   * element being the protocol name of the filesystem
   */
  Collection<Pair<WebFileSystemHandler, String>> getFileSystems();
}
