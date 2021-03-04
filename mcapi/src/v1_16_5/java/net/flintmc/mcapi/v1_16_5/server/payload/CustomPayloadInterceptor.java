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

package net.flintmc.mcapi.v1_16_5.server.payload;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.payload.PayloadChannelService;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;

;

@Singleton
public class CustomPayloadInterceptor {

  private static final int[] LOGGER_WARN_SEQUENCE =
      new int[]{Opcode.GETSTATIC, Opcode.LDC_W, Opcode.ALOAD_2, Opcode.INVOKEINTERFACE};

  private final ClassPool pool;
  private final ClassMapping customPayloadPacketMapping;
  private final ClassMapping clientPlayNetHandlerMapping;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private CustomPayloadInterceptor(
      ClassPool pool,
      ClassMappingProvider classMappingProvider,
      InjectedFieldBuilder.Factory fieldBuilderFactory) {
    this.pool = pool;
    this.customPayloadPacketMapping =
        classMappingProvider.get("net.minecraft.network.play.server.SCustomPayloadPlayPacket");
    this.clientPlayNetHandlerMapping = classMappingProvider
        .get("net.minecraft.client.network.play.ClientPlayNetHandler");
    this.fieldBuilderFactory = fieldBuilderFactory;
  }

  @ClassTransform(value = "net.minecraft.client.network.play.ClientPlayNetHandler")
  public void transform(ClassTransformContext context)
      throws NotFoundException, BadBytecode, CannotCompileException {

    CtClass[] parameters = new CtClass[]{this.pool.get(this.customPayloadPacketMapping.getName())};

    CtMethod method =
        context
            .getCtClass()
            .getDeclaredMethod(
                this.clientPlayNetHandlerMapping
                    .getMethod("handleCustomPayload", parameters).getName(),
                parameters);

    MethodInfo methodInfo = method.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    CodeIterator codeIterator = codeAttribute.iterator();

    int count = 0;

    CtField connectedInjectedService =
        this.fieldBuilderFactory
            .create()
            .target(method.getDeclaringClass())
            .inject(ConnectedServer.class)
            .generate();

    method.insertBefore(
        String.format(
            "{%s.retrieveCustomPayload($1.%s().toString(), $1.%s());}",
            connectedInjectedService.getName(),
            this.customPayloadPacketMapping.getMethod("getChannelName").getName(),
            this.customPayloadPacketMapping.getMethod("getBufferData").getName()));

    while (codeIterator.hasNext()) {
      int index = codeIterator.next();
      int opcode = codeIterator.byteAt(index);

      if (LOGGER_WARN_SEQUENCE[count] != opcode) {
        count = 0;
        continue;
      }

      ++count;
      if (count < LOGGER_WARN_SEQUENCE.length - 1) {
        continue;
      }

      int line = methodInfo.getLineNumber(index);

      CtField injectedService =
          this.fieldBuilderFactory
              .create()
              .target(method.getDeclaringClass())
              .inject(PayloadChannelService.class)
              .generate();

      method.insertAt(
          line + 2,
          String.format(
              "if (%s.shouldListen($1.%s().toString(), $1.%s())) {return;}",
              injectedService.getName(),
              this.customPayloadPacketMapping.getMethod("getChannelName").getName(),
              this.customPayloadPacketMapping.getMethod("getBufferData").getName()));
      break;
    }
  }
}
