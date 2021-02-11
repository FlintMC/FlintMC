package net.flintmc.mcapi.v1_16_5.entity.render;


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

  @ClassTransform(
      value = "net.minecraft.client.renderer.WorldVertexBufferUploader",
      version = "1.16.5")
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
        "net.flintmc.mcapi.v1_16_5.entity.render.WorldVertexBufferUploaderInterceptor$Handler.draw($1);");
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

      drawStateAccessor.getRenderCallbacks().forEach(Runnable::run);

      if (drawStateAccessor.getModelBoxHolder() == null) {
        return;
      }
      ModelBoxHolder modelBoxHolder = drawStateAccessor.getModelBoxHolder();
      if (modelBoxHolder.getContext().getRenderer() == null) {
        return;
      }
      modelBoxHolder
          .getContext()
          .getRenderer()
          .render(modelBoxHolder, drawStateAccessor.getRenderData());
    }
  }
}
