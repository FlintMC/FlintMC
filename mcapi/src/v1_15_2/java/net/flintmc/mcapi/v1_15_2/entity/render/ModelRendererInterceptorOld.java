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

import com.google.inject.Singleton;

@Singleton
public class ModelRendererInterceptorOld {
/*
  private static ModelRendererInterceptorOld INSTANCE;
  private final ClassMappingProvider classMappingProvider;
  private final EntityCache entityCache;
  private ClientPlayer clientPlayer;
  private Entity lastRenderedEntity;
  private MinecraftRenderMeta alternatingMinecraftRenderMeta;
  private boolean expectRenderCleanup = false;

  @Inject
  private ModelRendererInterceptorOld(
      ClassMappingProvider classMappingProvider, EntityCache entityCache) {
    this.classMappingProvider = classMappingProvider;
    this.entityCache = entityCache;
    INSTANCE = this;
  }

  public static boolean interceptRender(
      Object instance,
      MatrixStack matrixStackIn,
      IVertexBuilder bufferIn,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha) {
    return INSTANCE.render(
        instance, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
  }

  public static void interceptRenderCleanup(
      Object instance,
      MatrixStack matrixStackIn,
      IVertexBuilder bufferIn,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha) {
    INSTANCE.renderCleanup(
        instance, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
  }

  public static void interceptRotationAnglesUpdate(Object entity) {
    INSTANCE.rotationAnglesUpdate(entity);
  }

  @ClassTransform("net.minecraft.client.renderer.model.ModelRenderer")
  public void transform(ClassTransformContext classTransformContext) {
    try {
      CtClass[] parameters =
          ClassPool.getDefault()
              .get(
                  new String[] {
                    this.classMappingProvider
                        .get("com.mojang.blaze3d.matrix.MatrixStack")
                        .getName(),
                    this.classMappingProvider
                        .get("com.mojang.blaze3d.vertex.IVertexBuilder")
                        .getName(),
                    "int",
                    "int",
                    "float",
                    "float",
                    "float",
                    "float"
                  });

      CtMethod render =
          classTransformContext
              .getCtClass()
              .getDeclaredMethod(
                  classMappingProvider
                      .get("net.minecraft.client.renderer.model.ModelRenderer")
                      .getMethod("render", parameters)
                      .getName(),
                  parameters);

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

      render.insertBefore(
          "{if(net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRender($0,$$)){net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRenderCleanup($0, $$);return;}}");
      render.insertAfter(
          "{net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRenderCleanup($0, $$);}");

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
              "{net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRotationAnglesUpdate($1);}");

        break;
      }
    }
  }

  private void rotationAnglesUpdate(Object minecraftEntity) {
    Entity flintEntity =
        this.entityCache.getEntity(((net.minecraft.entity.Entity) minecraftEntity).getUniqueID());

    if (clientPlayer == null)
      clientPlayer = InjectionHolder.getInjectedInstance(ClientPlayer.class);

    if (flintEntity == null
        && this.clientPlayer
            .getUniqueId()
            .equals(((net.minecraft.entity.Entity) minecraftEntity).getUniqueID())) {
      flintEntity = this.clientPlayer;
    }
    if (flintEntity == null) return;
    for (ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder :
        flintEntity.getRenderContext().getRenderables().values()) {
      modelBoxHolder.callPropertyPreparations().callPropertyHandler();
    }
    this.lastRenderedEntity = flintEntity;
  }

  private void renderCleanup(
      Object instance,
      MatrixStack matrixStackIn,
      IVertexBuilder bufferIn,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha) {

    if (!expectRenderCleanup) return;
    if (lastRenderedEntity == null) return;
    ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
        lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
    if (modelBoxHolder == null) return;
    modelBoxHolder.callRenderCleanup();
    expectRenderCleanup = false;
  }

  @SuppressWarnings({"ConstantConditions"})
  public boolean render(
      Object instance,
      MatrixStack matrixStackIn,
      IVertexBuilder bufferIn,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha) {
    if (lastRenderedEntity == null) return false;
    ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
        lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
    if (modelBoxHolder == null) return false;
    modelBoxHolder.callRenderPreparations();
    if (modelBoxHolder.getContext().getRenderer() != null) {

      if (this.alternatingMinecraftRenderMeta == null) {
        this.alternatingMinecraftRenderMeta =
            InjectionHolder.getInjectedInstance(MinecraftRenderMeta.Factory.class).create();
      }

      MatrixStack.Entry lastMatrixStackEntry = matrixStackIn.getLast();
      Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) lastMatrixStackEntry.getMatrix();
      Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) lastMatrixStackEntry.getNormal();
      this.alternatingMinecraftRenderMeta
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

      this.alternatingMinecraftRenderMeta
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

      boolean render =
          modelBoxHolder
              .getContext()
              .getRenderer()
              .render(modelBoxHolder, this.alternatingMinecraftRenderMeta);

      ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) instance;
      modelRendererAccessor
          .getProperties()
          .putAll(modelBoxHolder.getPropertyContext().getProperties());
      System.out.println(modelRendererAccessor);
      if (render) {
        modelBoxHolder.callRenderCleanup();
      } else {
        expectRenderCleanup = true;
      }
      return render;
    } else {
      expectRenderCleanup = true;
      return false;
    }
  }*/
}
