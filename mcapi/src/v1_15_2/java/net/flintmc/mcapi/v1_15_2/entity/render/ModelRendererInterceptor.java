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
import com.google.inject.name.Named;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.matrix.MatrixStack.Entry;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javassist.*;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.internal.entity.DefaultEntityRepository;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.Renderer;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookFilter;
import net.flintmc.transform.hook.HookFilters;
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
import java.util.Arrays;
import java.util.List;

@Singleton
public class ModelRendererInterceptor {

  private static Constructor<BufferBuilder.DrawState> drawStateConstructor;
  private final ClassMappingProvider classMappingProvider;

  @Inject
  private ModelRendererInterceptor(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  static {
    try {
      drawStateConstructor =
          BufferBuilder.DrawState.class.getDeclaredConstructor(
              VertexFormat.class, int.class, int.class);
      drawStateConstructor.setAccessible(true);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @ClassTransform(value = "net.minecraft.client.renderer.model.ModelRenderer")
  public void transform(ClassTransformContext classTransformContext) {
    try {
      String matrixStackEntryName = this.classMappingProvider
          .get("com.mojang.blaze3d.matrix.MatrixStack$Entry").getName();

      String iVertexBuilderName = this.classMappingProvider
          .get("com.mojang.blaze3d.vertex.IVertexBuilder").getName();

      CtClass[] doRenderParameters =
          ClassPool.getDefault()
              .get(
                  new String[]{
                      matrixStackEntryName,
                      iVertexBuilderName,
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
          "{\n" +
              "    net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.Handler.interceptDoRender($0, $$);\n"
              + "    "
              + "if(net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.Handler.shouldCancel($0, $1, $3)){\n"
              +
              "        "
              + "return;\n"
              + "    }\n"
              + "}");

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
                new String[]{
                    classMappingProvider.get("net.minecraft.entity.Entity").getName(), "float",
                    "float", "float", "float", "float"
                });

    for (CtMethod declaredMethod : classTransformContext.getCtClass().getDeclaredMethods()) {
      if (Arrays.equals(declaredMethod.getParameterTypes(), classes) && declaredMethod
          .getName()
          .equals(
              this.classMappingProvider
                  .get("net.minecraft.client.renderer.entity.model.EntityModel")
                  .getMethod("setRotationAngles", classes)
                  .getName())) {
        if (!Modifier.isAbstract(declaredMethod.getModifiers())) {
          declaredMethod.insertAfter(
              "{net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.Handler.interceptRotationAnglesUpdate($1);}");
        }

        break;
      }
    }
  }

  @HookFilter(
      value = HookFilters.SUBCLASS_OF,
      type = @Type(typeName = "net.minecraft.client.renderer.entity.model.EntityModel"))
  @Hook(
      methodName = "setRotationAngles",
      parameters = {
          @Type(typeName = "net.minecraft.entity.Entity"),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class)
      })
  public static void setRotationAngles(@Named("args") Object[] args) {
    Handler.interceptRotationAnglesUpdate(args[0]);
  }

  @Singleton
  public static class Handler {

    private static final Handler INSTANCE = InjectionHolder.getInjectedInstance(Handler.class);

    private final DefaultEntityRepository entityRepository;
    private final ClientPlayer clientPlayer;
    private final MinecraftRenderMeta alternatingMinecraftRenderMeta;
    private Entity lastRenderedEntity;

    @Inject
    private Handler(
        DefaultEntityRepository entityRepository,
        ClientPlayer clientPlayer,
        MinecraftRenderMeta.Factory minecraftRenderMetaFactory) {
      this.entityRepository = entityRepository;
      this.clientPlayer = clientPlayer;
      this.alternatingMinecraftRenderMeta = minecraftRenderMetaFactory.create();
    }


    public static void interceptRotationAnglesUpdate(Object instance) {

      net.minecraft.entity.Entity minecraftEntity = (net.minecraft.entity.Entity) instance;

      Entity flintEntity = INSTANCE.entityRepository.getEntity((minecraftEntity).getUniqueID());

      if (flintEntity == null
          && INSTANCE.clientPlayer.getUniqueId().equals(minecraftEntity.getUniqueID())) {
        flintEntity = INSTANCE.clientPlayer;
      }
      if (flintEntity == null) {
        return;
      }
      for (ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder :
          flintEntity.getRenderContext().getRenderables().values()) {
        modelBoxHolder.callPropertyPreparations().callPropertyHandler().callRenderPreparations();
      }
      INSTANCE.lastRenderedEntity = flintEntity;
    }

    public static boolean shouldCancel(Object instance,
        Object matrixEntryObject,
        int packedLightIn) {

      ModelRenderer modelRenderer = (ModelRenderer) instance;
      MatrixStack.Entry matrixEntry = (MatrixStack.Entry) matrixEntryObject;

      if (INSTANCE.lastRenderedEntity == null) {
        return false;
      }
      ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
          INSTANCE.lastRenderedEntity.getRenderContext().getRenderableByTarget(modelRenderer);
      if (modelBoxHolder == null) {
        INSTANCE.lastRenderedEntity.updateRenderables();
        modelBoxHolder =
            INSTANCE.lastRenderedEntity.getRenderContext().getRenderableByTarget(modelRenderer);
        if (modelBoxHolder == null) {
          return false;
        }
      }
      if (modelBoxHolder.getContext().getRenderer() != null) {

        Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) matrixEntry.getMatrix();
        Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) matrixEntry.getNormal();

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
            .setPartialTick(Minecraft.getInstance().getRenderPartialTicks())
            .setTargetUuid(INSTANCE.lastRenderedEntity.getUniqueId());

        return !modelBoxHolder
            .getContext()
            .getRenderer()
            .shouldExecuteNextStage(modelBoxHolder, INSTANCE.alternatingMinecraftRenderMeta);
      } else {
        return false;
      }

    }

    public static boolean interceptDoRender(
        Object modelRenderer,
        Object matrixEntry,
        Object buffer,
        int packedLightIn,
        int packedOverlayIn,
        float red,
        float green,
        float blue,
        float alpha) {

      ModelRenderer instance = (ModelRenderer) modelRenderer;
      MatrixStack.Entry matrixEntryIn = (Entry) matrixEntry;

      if (INSTANCE.lastRenderedEntity == null) {
        return false;
      }
      ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
          INSTANCE.lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
      if (modelBoxHolder == null) {
        return false;
      }

      ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) instance;

      if (modelRendererAccessor.getMatrixHandler() != null) {
        modelRendererAccessor.getMatrixHandler().accept(matrixEntryIn);
      }

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
            .setPartialTick(Minecraft.getInstance().getRenderPartialTicks())
            .setTargetUuid(INSTANCE.lastRenderedEntity.getUniqueId());

        boolean cancelRender =
            !modelBoxHolder
                .getContext()
                .getRenderer()
                .shouldExecuteNextStage(modelBoxHolder, INSTANCE.alternatingMinecraftRenderMeta);

        modelRendererAccessor
            .getProperties()
            .putAll(modelBoxHolder.getPropertyContext().getProperties());

        doRender(
            instance,
            matrixEntryIn,
            (IVertexBuilder) buffer,
            packedLightIn,
            packedOverlayIn,
            red,
            green,
            blue,
            alpha,
            modelBoxHolder.getContext().getRenderer(),
            modelBoxHolder,
            INSTANCE.alternatingMinecraftRenderMeta);
        modelBoxHolder.callRenderCleanup();
        return cancelRender;
      } else {
        modelBoxHolder.callRenderCleanup();
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

      ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) modelRenderer;

      if (renderer != null) {
        List<BufferBuilder.DrawState> drawStates = ((BufferBuilderAccessor) buffer).getDrawStates();
        try {
          BufferBuilder.DrawState drawState;

          drawState =
              drawStateConstructor.newInstance(
                  ((BufferBuilderAccessor) buffer).getVertexFormat(), 0, 0);
          DrawStateAccessor drawStateAccessor = (DrawStateAccessor) (Object) drawState;

          drawStateAccessor.setModelRenderData(renderMeta);
          drawStateAccessor.setModelBoxHolder(modelBoxHolder);
          drawStates.add(drawState);

        } catch (IllegalAccessException
            | InstantiationException
            | InvocationTargetException e) {
          e.printStackTrace();
        }
        if (!renderer.shouldExecuteNextStage(modelBoxHolder, renderMeta)) {
          return;
        }
      }

      Matrix4f matrix4f = matrixStackEntry.getMatrix();
      Matrix3f matrix3f = matrixStackEntry.getNormal();

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
