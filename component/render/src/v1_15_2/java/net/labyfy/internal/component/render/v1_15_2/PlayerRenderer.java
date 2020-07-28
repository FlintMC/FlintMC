package net.labyfy.internal.component.render.v1_15_2;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import javassist.CannotCompileException;
import javassist.CtConstructor;
import javassist.NotFoundException;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.render.RenderType;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexFormatElement;
import net.labyfy.component.transform.asm.MethodVisit;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@AutoLoad
@Singleton
public class PlayerRenderer {

  private final VertexBufferTranslator vertexBufferTranslator;

  @Inject
  private PlayerRenderer(VertexBufferTranslator vertexBufferTranslator) {
    this.vertexBufferTranslator = vertexBufferTranslator;
  }

  public static void test123() {
    try {
      Field renderTypeTextures = WorldRenderer.class.getDeclaredField("renderTypeTextures");
      renderTypeTextures.setAccessible(true);
      RenderTypeBuffers renderTypeBuffers = (RenderTypeBuffers) renderTypeTextures.get(Minecraft.getInstance().worldRenderer);
      renderTypeBuffers.getBufferSource().finish(Holder.cosmetic.getHandle());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static void modifyPlayerRenderer(Object instance, boolean smallArms, List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layers) {
    layers.add(new LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>((IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>) instance) {
      public void render(MatrixStack matrixStackHandle, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexBuffer vertexBuffer = new VertexBufferProviderImpl(iRenderTypeBuffer).get(Holder.cosmetic);
        MatrixStackImpl matrixStack = new MatrixStackImpl(vertexBuffer, matrixStackHandle);
        matrixStack.push();

        float f = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRotationYaw, entitylivingbaseIn.rotationYaw) - MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset);
        matrixStack.rotate(new Quaternionf().rotateY((float) Math.toRadians(f + ageInTicks / 2)));
        matrixStack.translate(0, (float) Math.cos(ageInTicks / 10) / 20f, 0);
        matrixStack.scale(0.0625F);

        for (int i = 0; i < 4; i++) {
          vertexBuffer
              .box(-3.0F, -12.5F, -4.0F, 6, 1, 1);
          matrixStack.rotate(new Quaternionf().rotateY((float) Math.toRadians(90)));
        }
        matrixStackHandle.pop();
      }
    });
  }

  @ClassTransform("net.minecraft.client.renderer.entity.PlayerRenderer")
  public void on(ClassTransformContext classTransformContext) {
    for (CtConstructor declaredConstructor : classTransformContext.getCtClass().getDeclaredConstructors()) {
      try {
        if (declaredConstructor.getParameterTypes().length == 2) {
          declaredConstructor.insertAfter("net.labyfy.internal.component.render.v1_15_2.PlayerRenderer.modifyPlayerRenderer($0, $2, layerRenderers);");
        }
      } catch (NotFoundException | CannotCompileException e) {
        e.printStackTrace();
      }
    }
  }

  @MethodVisit(
      className = "net.minecraft.client.renderer.WorldRenderer",
      methodName = "updateCameraAndRender",
      desc = "(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/renderer/Matrix4f;)V"
  )
  public void visit(MethodVisitorContext methodVisitorContext) {
    methodVisitorContext.storeAsset("finished", false);
    methodVisitorContext.onVisitMethodInsn((context) -> {
      context.write();
      if (context.getName().equals("finish")) {
        if (!methodVisitorContext.<Boolean>getAsset("finished")) {
          methodVisitorContext.storeAsset("finished", true);
          methodVisitorContext.svisitMethodInsn(Opcodes.INVOKESTATIC, "net/labyfy/internal/component/render/v1_15_2/PlayerRenderer", "test123", "()V");
        }
      }
    });
  }

  public static class Holder {
    public static RenderType cosmetic = new RenderTypeImpl(new VertexFormatImpl(new VertexFormatElementImpl[]{
        new VertexFormatElementImpl("position", VertexFormatElement.Type.FLOAT, 3)
    }), "Test123", GL_TRIANGLES)
        .transparency(RenderSystem::disableBlend, () -> {
        })
        .cull(false)
        .diffuseLighting(true)
        .lightmap(true)
        .overlay(false);

  }

}
