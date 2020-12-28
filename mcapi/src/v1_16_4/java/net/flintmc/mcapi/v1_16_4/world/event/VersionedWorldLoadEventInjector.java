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

package net.flintmc.mcapi.v1_16_4.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class VersionedWorldLoadEventInjector {

  private static final String LISTENER = "net.minecraft.world.chunk.listener.IChunkStatusListener";

  private final ClassMappingProvider mappingProvider;

  @Inject
  private VersionedWorldLoadEventInjector(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform("net.minecraft.world.server.ChunkManager")
  public void transformChunkManager(ClassTransformContext context) throws CannotCompileException {
    CtClass transforming = context.getCtClass();
    CtConstructor constructor = transforming.getDeclaredConstructors()[0];

    constructor.insertBeforeBody(
        String.format(
            "%s delegate = $9; $9 = new %s(delegate, $1.%s().%s());",
            LISTENER,
            DelegatingChunkStatusListener.class.getName(),
            this.mappingProvider
                .get("net.minecraft.world.World")
                .getMethod("getWorldInfo")
                .getName(),
            this.mappingProvider
                .get("net.minecraft.world.storage.WorldInfo")
                .getMethod("getWorldName")
                .getName()));
  }
}
