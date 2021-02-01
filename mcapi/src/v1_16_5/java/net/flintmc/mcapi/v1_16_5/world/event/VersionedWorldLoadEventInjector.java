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

package net.flintmc.mcapi.v1_16_5.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;

@Singleton
public class VersionedWorldLoadEventInjector {

  private static final String LISTENER = "net.minecraft.world.chunk.listener.IChunkStatusListener";
  private static final String SERVER_WORLD_INFO = "net.minecraft.world.storage.IServerWorldInfo";

  private final ClassMappingProvider mappingProvider;

  @Inject
  private VersionedWorldLoadEventInjector(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform(value = "net.minecraft.world.server.ServerWorld", version = "1.16.5")
  public void transformServerWorld(ClassTransformContext context) throws CannotCompileException {
    CtClass transforming = context.getCtClass();
    ClassMapping classMapping = this.mappingProvider.get("net.minecraft.world.server.ServerWorld");
    FieldMapping fieldMapping = classMapping.getField("field_241103_E_");

    transforming.addMethod(
        CtMethod.make(
            String.format(
                "public %s getServerWorldInfo() {" + "return %s;" + "}",
                this.mappingProvider.get(SERVER_WORLD_INFO).getName(), fieldMapping.getName()),
            transforming));
  }

  @ClassTransform(value = "net.minecraft.world.server.ChunkManager", version = "1.16.5")
  public void transformChunkManager(ClassTransformContext context) throws CannotCompileException {
    CtClass transforming = context.getCtClass();
    CtConstructor constructor = transforming.getDeclaredConstructors()[0];

    constructor.insertBeforeBody(
        String.format(
            "%s delegate = $9; $9 = new %s(delegate, $1.%s().%s());",
            LISTENER,
            DelegatingChunkStatusListener.class.getName(),
            "getServerWorldInfo",
            this.mappingProvider
                .get("net.minecraft.world.storage.IServerWorldInfo")
                .getMethod("getWorldName")
                .getName()));
  }
}
