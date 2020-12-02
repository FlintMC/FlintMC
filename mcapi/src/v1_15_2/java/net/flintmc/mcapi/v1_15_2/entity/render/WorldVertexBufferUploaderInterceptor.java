package net.flintmc.mcapi.v1_15_2.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import javassist.*;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

@Singleton
public class WorldVertexBufferUploaderInterceptor {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private WorldVertexBufferUploaderInterceptor(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform("net.minecraft.client.renderer.WorldVertexBufferUploader")
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
                new CtClass[] {bufferBuilderClass});

    draw.setBody(
        "net.flintmc.mcapi.v1_15_2.entity.render.WorldVertexBufferUploaderInterceptor$Handler.draw($1);");
  }

  public static class Handler {
    public static void draw(BufferBuilder bufferBuilderIn) {
      if (!RenderSystem.isOnRenderThread()) {
        RenderSystem.recordRenderCall(
            () -> {
              Pair<BufferBuilder.DrawState, ByteBuffer> pair1 = bufferBuilderIn.getNextBuffer();
              BufferBuilder.DrawState drawState = pair1.getFirst();
              draw(
                  pair1,
                  drawState.getDrawMode(),
                  drawState.getFormat(),
                  drawState.getVertexCount());
            });
      } else {
        Pair<BufferBuilder.DrawState, ByteBuffer> pair = bufferBuilderIn.getNextBuffer();
        BufferBuilder.DrawState drawState = pair.getFirst();
        draw(pair, drawState.getDrawMode(), drawState.getFormat(), drawState.getVertexCount());
      }
    }

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
      if (drawStateAccessor.getModelBoxHolder() == null) return;
      ModelBoxHolder<?, ?> modelBoxHolder = drawStateAccessor.getModelBoxHolder();

    }
  }
}
