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

package net.flintmc.mcapi.v1_15_2.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.system.MemoryUtil;

@Singleton
public class WorldVertexBufferUploaderInterceptor {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private WorldVertexBufferUploaderInterceptor(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform(value = "net.minecraft.client.renderer.WorldVertexBufferUploader", version = "1.15.2")
  public void transform(ClassTransformContext classTransformContext)
      throws NotFoundException, CannotCompileException {
    CtClass bufferBuilderClass =
        ClassPool.getDefault().get("net.minecraft.client.renderer.BufferBuilder");
    CtMethod draw =
        classTransformContext
            .getCtClass()
            .getDeclaredMethod(
                classMappingProvider
                    .get("net.minecraft.client.renderer.WorldVertexBufferUploader")
                    .getMethod("draw", bufferBuilderClass)
                    .getName(),
                new CtClass[]{bufferBuilderClass});

    draw.setBody(
        "net.flintmc.mcapi.v1_15_2.entity.render.WorldVertexBufferUploaderInterceptor$Handler.draw($1);");
  }

  public static class Handler {

    public static void draw(BufferBuilder bufferBuilderIn) {
      if (!RenderSystem.isOnRenderThread()) {
        RenderSystem.recordRenderCall(
            () -> {
              while (!((BufferBuilderAccessor) bufferBuilderIn).getDrawStates().isEmpty()) {
                Pair<BufferBuilder.DrawState, ByteBuffer> pair1 = bufferBuilderIn.getNextBuffer();
                BufferBuilder.DrawState drawState = pair1.getFirst();
                draw(
                    pair1,
                    drawState.getDrawMode(),
                    drawState.getFormat(),
                    drawState.getVertexCount());
              }
            });
      } else {
        while (!((BufferBuilderAccessor) bufferBuilderIn).getDrawStates().isEmpty()) {
          Pair<BufferBuilder.DrawState, ByteBuffer> pair = bufferBuilderIn.getNextBuffer();
          BufferBuilder.DrawState drawState = pair.getFirst();
          draw(pair, drawState.getDrawMode(), drawState.getFormat(), drawState.getVertexCount());
        }
      }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void draw(
        Pair<BufferBuilder.DrawState, ByteBuffer> pair,
        int modeIn,
        VertexFormat vertexFormatIn,
        int countIn) {
      ByteBuffer bufferIn = pair.getSecond();
      RenderSystem.assertThread(RenderSystem::isOnRenderThread);
      bufferIn.clear();
      if (countIn > 0) {
        vertexFormatIn.setupBufferState(MemoryUtil.memAddress(bufferIn));
        GlStateManager.drawArrays(modeIn, 0, countIn);
        vertexFormatIn.clearBufferState();
      }
      DrawStateAccessor drawStateAccessor = (DrawStateAccessor) (Object) pair.getFirst();
      if (drawStateAccessor.getModelBoxHolder() == null) {
        return;
      }
      ModelBoxHolder modelBoxHolder = drawStateAccessor.getModelBoxHolder();
      if (modelBoxHolder.getContext().getRenderer() == null) {
        return;
      }
      modelBoxHolder.getContext().getRenderer()
          .render(modelBoxHolder, drawStateAccessor.getRenderData());
    }
  }
}
