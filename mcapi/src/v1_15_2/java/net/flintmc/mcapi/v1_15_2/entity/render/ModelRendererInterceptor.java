package net.flintmc.mcapi.v1_15_2.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javassist.*;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.internal.entity.cache.EntityCache;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.Renderer;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.CtClassFilters;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Singleton
public class ModelRendererInterceptor {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private ModelRendererInterceptor(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform("net.minecraft.client.renderer.model.ModelRenderer")
  public void transform(ClassTransformContext classTransformContext) {
    try {
      CtClass[] doRenderParameters =
          ClassPool.getDefault()
              .get(
                  new String[] {
                      "com.mojang.blaze3d.matrix.MatrixStack$Entry",
                      "com.mojang.blaze3d.vertex.IVertexBuilder",
                      "int",
                      "int",
                      "float",
                      "float",
                      "float",
                      "float"
                  });

      CtMethod doRender =
          classTransformContext
              .getCtClass()
              .getDeclaredMethod(
                  classMappingProvider
                      .get("net.minecraft.client.renderer.model.ModelRenderer")
                      .getMethod("doRender", doRenderParameters)
                      .getName(),
                  doRenderParameters);

      doRender.insertBefore(
          "{if(net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.Handler.interceptDoRender($0,$$)){"
              +
              //
              // "net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRenderCleanup($0, $$);" +
              "return;}}");
      //      render.insertAfter(
      //
      // "{net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRenderCleanup($0, $$);}");

    } catch (NotFoundException | CannotCompileException e) {
      e.printStackTrace();
    }
  }

  @ClassTransform
  @CtClassFilter(
      value = CtClassFilters.SUBCLASS_OF,
      className = "net.minecraft.client.renderer.entity.model.EntityModel")
  public void transform2(ClassTransformContext classTransformContext)
      throws NotFoundException, CannotCompileException {
    CtClass[] classes =
        ClassPool.getDefault()
            .get(
                new String[] {
                    "net.minecraft.entity.Entity", "float", "float", "float", "float", "float"
                });

    for (CtMethod declaredMethod : classTransformContext.getCtClass().getDeclaredMethods()) {
      if (declaredMethod
          .getName()
          .equals(
              this.classMappingProvider
                  .get("net.minecraft.client.renderer.entity.model.EntityModel")
                  .getMethod("setRotationAngles", classes)
                  .getName())) {
        if (!Modifier.isAbstract(declaredMethod.getModifiers()))
          declaredMethod.insertAfter(
              "{net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.Handler.interceptRotationAnglesUpdate($1);}");

        break;
      }
    }
  }

  @Singleton
  public static class Handler {

    private static final Handler INSTANCE = InjectionHolder.getInjectedInstance(Handler.class);

    private final EntityCache entityCache;
    private final ClientPlayer clientPlayer;
    private final MinecraftRenderMeta alternatingMinecraftRenderMeta;
    private Entity lastRenderedEntity;

    @Inject
    private Handler(
        EntityCache entityCache,
        ClientPlayer clientPlayer,
        MinecraftRenderMeta.Factory minecraftRenderMetaFactory) {
      this.entityCache = entityCache;
      this.clientPlayer = clientPlayer;
      this.alternatingMinecraftRenderMeta = minecraftRenderMetaFactory.create();
    }

    public static void interceptRotationAnglesUpdate(net.minecraft.entity.Entity minecraftEntity) {

      Entity flintEntity = INSTANCE.entityCache.getEntity((minecraftEntity).getUniqueID());

      if (flintEntity == null
          && INSTANCE.clientPlayer.getUniqueId().equals(minecraftEntity.getUniqueID())) {
        flintEntity = INSTANCE.clientPlayer;
      }
      if (flintEntity == null) return;
      for (ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder :
          flintEntity.getRenderContext().getRenderables().values()) {
        modelBoxHolder.callPropertyPreparations().callPropertyHandler();
      }
      INSTANCE.lastRenderedEntity = flintEntity;
    }

    public static boolean interceptDoRender(
        ModelRenderer instance,
        MatrixStack.Entry matrixEntryIn,
        IVertexBuilder bufferIn,
        int packedLightIn,
        int packedOverlayIn,
        float red,
        float green,
        float blue,
        float alpha) {

      if (INSTANCE.lastRenderedEntity == null) return false;
      ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
          INSTANCE.lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
      if (modelBoxHolder == null) return false;
      modelBoxHolder.callRenderPreparations();
      if (modelBoxHolder.getContext().getRenderer() != null) {

        Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) matrixEntryIn.getMatrix();
        Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) matrixEntryIn.getNormal();

        INSTANCE
            .alternatingMinecraftRenderMeta
            .getWorld()
            .set(
                worldMatrix.getM00(),
                worldMatrix.getM10(),
                worldMatrix.getM20(),
                worldMatrix.getM30(),
                worldMatrix.getM01(),
                worldMatrix.getM11(),
                worldMatrix.getM21(),
                worldMatrix.getM31(),
                worldMatrix.getM02(),
                worldMatrix.getM12(),
                worldMatrix.getM22(),
                worldMatrix.getM32(),
                worldMatrix.getM03(),
                worldMatrix.getM13(),
                worldMatrix.getM23(),
                worldMatrix.getM33());

        INSTANCE
            .alternatingMinecraftRenderMeta
            .getNormal()
            .set(
                normalMatrix.getM00(),
                normalMatrix.getM10(),
                normalMatrix.getM20(),
                normalMatrix.getM01(),
                normalMatrix.getM11(),
                normalMatrix.getM21(),
                normalMatrix.getM02(),
                normalMatrix.getM12(),
                normalMatrix.getM22());

        INSTANCE
            .alternatingMinecraftRenderMeta
            .setPackedLight(packedLightIn)
            .setPartialTick(Minecraft.getInstance().getRenderPartialTicks());

        boolean cancelRender =
            !modelBoxHolder
                .getContext()
                .getRenderer()
                .shouldExecuteNextStage(modelBoxHolder, INSTANCE.alternatingMinecraftRenderMeta);

        ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) instance;
        modelRendererAccessor
            .getProperties()
            .putAll(modelBoxHolder.getPropertyContext().getProperties());

        doRender(
            instance,
            matrixEntryIn,
            bufferIn,
            packedLightIn,
            packedOverlayIn,
            red,
            green,
            blue,
            alpha,
            modelBoxHolder.getContext().getRenderer(),
            modelBoxHolder,
            INSTANCE.alternatingMinecraftRenderMeta);
        if (cancelRender) {
          modelBoxHolder.callRenderCleanup();
        } else {
        }
        return cancelRender;
      } else {
        return false;
      }
    }

    @SuppressWarnings("unchecked")
    public static void doRender(
        ModelRenderer modelRenderer,
        MatrixStack.Entry matrixStackEntry,
        IVertexBuilder buffer,
        int packedLight,
        int packedOverlay,
        float red,
        float green,
        float blue,
        float alpha,
        Renderer renderer,
        ModelBoxHolder modelBoxHolder,
        MinecraftRenderMeta renderMeta) {

      if (renderer != null) {
        Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) matrixStackEntry.getMatrix();
        Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) matrixStackEntry.getNormal();

        List<BufferBuilder.DrawState> drawStates = ((BufferBuilderAccessor) buffer).getDrawStates();
        try {
          BufferBuilder.DrawState drawState;

          Constructor<?> declaredConstructor1 =
              BufferBuilder.DrawState.class.getDeclaredConstructor(
                  VertexFormat.class, int.class, int.class);
          declaredConstructor1.setAccessible(true);
          drawState =
              (BufferBuilder.DrawState)
                  declaredConstructor1.newInstance(
                      ((BufferBuilderAccessor) buffer).getVertexFormat(), 0, 0);
          DrawStateAccessor drawStateAccessor = (DrawStateAccessor) (Object) drawState;

          drawStateAccessor.setModelRenderData(renderMeta);
          drawStateAccessor.setModelBoxHolder(modelBoxHolder);
          drawStates.add(drawState);

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
          e.printStackTrace();
        }
        if (!renderer.shouldExecuteNextStage(modelBoxHolder, renderMeta)) {
          return;
        }
      }

      Matrix4f matrix4f = matrixStackEntry.getMatrix();
      Matrix3f matrix3f = matrixStackEntry.getNormal();

      ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) modelRenderer;

      for (ModelRenderer.ModelBox modelBox : modelRendererAccessor.getModelBoxes()) {
        ModelBoxAccessor modelBoxAccessor = (ModelBoxAccessor) modelBox;
        for (TexturedQuadAccessor quad : modelBoxAccessor.getQuads()) {
          Vector3f vector3f = quad.getNormal().copy();
          vector3f.transform(matrix3f);
          float f = vector3f.getX();
          float f1 = vector3f.getY();
          float f2 = vector3f.getZ();

          for (int i = 0; i < 4; ++i) {
            PositionTextureVertexAccessor modelrenderer$positiontexturevertex =
                quad.getVertexPositions()[i];
            float f3 = modelrenderer$positiontexturevertex.getPosition().getX() / 16.0F;
            float f4 = modelrenderer$positiontexturevertex.getPosition().getY() / 16.0F;
            float f5 = modelrenderer$positiontexturevertex.getPosition().getZ() / 16.0F;
            Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
            vector4f.transform(matrix4f);

            buffer.addVertex(
                vector4f.getX(),
                vector4f.getY(),
                vector4f.getZ(),
                red,
                green,
                blue,
                alpha,
                modelrenderer$positiontexturevertex.getTextureU(),
                modelrenderer$positiontexturevertex.getTextureV(),
                packedOverlay,
                packedLight,
                f,
                f1,
                f2);
          }
        }
      }
    }
  }
}