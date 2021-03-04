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

package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.InjectedFieldBuilder.Factory;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.CtClassFilters;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class PacketEventInjectionTransformer {

  private final ClassMappingProvider mappingProvider;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private PacketEventInjectionTransformer(
      ClassMappingProvider mappingProvider, Factory fieldBuilderFactory) {
    this.mappingProvider = mappingProvider;
    this.fieldBuilderFactory = fieldBuilderFactory;
  }

  @ClassTransform
  @CtClassFilter(value = CtClassFilters.SUBCLASS_OF, className = "net.minecraft.network.IPacket")
  public void transformPacket(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass packet = context.getCtClass();

    CtClass netHandler = packet.getClassPool()
        .get(this.mappingProvider.get("net.minecraft.network.INetHandler").getName());

    for (CtMethod method : packet.getDeclaredMethods()) {
      CtClass[] params = method.getParameterTypes();
      if (params.length != 1 || !params[0].subtypeOf(netHandler)) {
        continue;
      }

      CtField injectorField = fieldBuilderFactory.create()
          .target(packet)
          .inject(PacketEventInjector.class)
          .generate();

      method.insertAfter(
          String.format("{ %s.processIncomingPacket(new Object[]{$0}, %s.AFTER); }",
              injectorField.getName(),
              ExecutionTime.class.getName()));
      break;
    }
  }
}
