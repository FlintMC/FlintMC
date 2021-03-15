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

package net.flintmc.util.classcache;

import com.google.inject.name.Named;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import java.util.UUID;

public interface CachedClass {

  String CACHED_CLASS_PATH = "flint/class-cache/{UUID}.bin";

  byte[] read();

  void write(byte[] bytecode);

  UUID getUUID();

  String getName();

  boolean hasBytecode();

  @AssistedFactory(CachedClass.class)
  interface Factory {

    CachedClass create(@Named("name") String name, @Named("uuid") UUID uuid);

  }

}
