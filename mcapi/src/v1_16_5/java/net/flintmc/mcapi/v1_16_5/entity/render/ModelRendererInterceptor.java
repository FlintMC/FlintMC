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

package net.flintmc.mcapi.v1_16_5.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
import net.flintmc.transform.hook.HookResult;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.BufferBuilder.DrawState;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

@Singleton
public class ModelRendererInterceptor {

  private final ClassMappingProvider classMappingProvider;
  private final DefaultEntityRepository entityRepository;
  private final ClientPlayer clientPlayer;
  private final MinecraftRenderMeta alternatingMinecraftRenderMeta;
  private Entity lastRenderedEntity;

  private static Constructor<BufferBuilder.DrawState> drawStateConstructor;

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

  @Inject
  private ModelRendererInterceptor(
      ClassMappingProvider classMappingProvider,
      DefaultEntityRepository entityRepository,
      ClientPlayer clientPlayer,
      MinecraftRenderMeta.Factory minecraftRenderMetaFactory) {
    this.classMappingProvider = classMappingProvider;
    this.entityRepository = entityRepository;
    this.clientPlayer = clientPlayer;
    this.alternatingMinecraftRenderMeta = minecraftRenderMetaFactory.create();
  }

  @Hook(
      className = "net.minecraft.client.renderer.model.ModelRenderer",
      methodName = "doRender",
      parameters = {
          @Type(typeName = "com.mojang.blaze3d.matrix.MatrixStack$Entry"),
          @Type(typeName = "com.mojang.blaze3d.vertex.IVertexBuilder"),
          @Type(reference = int.class),
          @Type(reference = int.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class)
      }
  )
  public HookResult hookModelRender(
      @Named("instance") Object rawInstance, @Named("args") Object[] args) {
    ModelRenderer instance = (ModelRenderer) rawInstance;
    MatrixStack.Entry matrixEntryIn = (MatrixStack.Entry) args[0];
    IVertexBuilder bufferIn = (IVertexBuilder) args[1];
    int packedLightIn = (int) args[2];
    int packedOverlayIn = (int) args[3];
    float red = (float) args[4];
    float green = (float) args[5];
    float blue = (float) args[6];
    float alpha = (float) args[7];

    if (this.lastRenderedEntity == null) {
      return this.shouldCancelRender(instance, matrixEntryIn, packedLightIn);
    }
    ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
        this.lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
    if (modelBoxHolder == null) {
      this.lastRenderedEntity.updateRenderables();
      modelBoxHolder =
          this.lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
      if (modelBoxHolder == null) {
        return this.shouldCancelRender(instance, matrixEntryIn, packedLightIn);
      }
    }
    modelBoxHolder.callRenderPreparations();
    if (modelBoxHolder.getContext().getRenderer() != null) {

      Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) matrixEntryIn.getMatrix();
      Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) matrixEntryIn.getNormal();

      this
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

      this
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

      this
          .alternatingMinecraftRenderMeta
          .setPackedLight(packedLightIn)
          .setPartialTick(Minecraft.getInstance().getRenderPartialTicks())
          .setTargetUuid(this.lastRenderedEntity.getUniqueId());

      modelBoxHolder
          .getContext()
          .getRenderer()
          .shouldExecuteNextStage(modelBoxHolder, this.alternatingMinecraftRenderMeta);

      ModelRendererAccessor modelRendererAccessor = (ModelRendererAccessor) instance;
      modelRendererAccessor
          .getProperties()
          .putAll(modelBoxHolder.getPropertyContext().getProperties());

      this.doRender(
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
          this.alternatingMinecraftRenderMeta);
      modelBoxHolder.callRenderCleanup();

      return this.shouldCancelRender(instance, matrixEntryIn, packedLightIn);
    } else {
      modelBoxHolder.callRenderCleanup();
      return this.shouldCancelRender(instance, matrixEntryIn, packedLightIn);
    }
  }

  private HookResult shouldCancelRender(ModelRenderer instance, MatrixStack.Entry matrixEntryIn,
      int packedLightIn) {
    if (this.lastRenderedEntity == null) {
      return HookResult.CONTINUE;
    }
    ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder =
        this.lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
    if (modelBoxHolder == null) {
      return HookResult.CONTINUE;
    }
    if (modelBoxHolder.getContext().getRenderer() != null) {

      Matrix4fAccessor worldMatrix = (Matrix4fAccessor) (Object) matrixEntryIn.getMatrix();
      Matrix3fAccessor normalMatrix = (Matrix3fAccessor) (Object) matrixEntryIn.getNormal();

      this
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

      this
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

      this
          .alternatingMinecraftRenderMeta
          .setPackedLight(packedLightIn)
          .setPartialTick(Minecraft.getInstance().getRenderPartialTicks())
          .setTargetUuid(this.lastRenderedEntity.getUniqueId());

      return !modelBoxHolder
          .getContext()
          .getRenderer()
          .shouldExecuteNextStage(modelBoxHolder, this.alternatingMinecraftRenderMeta)
          ? HookResult.BREAK : HookResult.CONTINUE;
    } else {
      return HookResult.CONTINUE;
    }
  }

  @SuppressWarnings("unchecked")
  private void doRender(
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
      List<DrawState> drawStates = ((BufferBuilderAccessor) buffer).getDrawStates();
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

  @HookFilter(
      value = HookFilters.SUBCLASS_OF,
      type = @Type(typeName = "net.minecraft.client.renderer.entity.model.EntityModel")
  )
  @Hook(
      methodName = "setRotationAngles",
      parameters = {
          @Type(typeName = "net.minecraft.entity.Entity"),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class),
          @Type(reference = float.class)
      }
  )
  public void interceptRotationAnglesUpdate(@Named("args") Object[] args) {
    net.minecraft.entity.Entity entity = (net.minecraft.entity.Entity) args[0];

    Entity flintEntity = this.entityRepository.getEntity(entity.getUniqueID());

    if (flintEntity == null && this.clientPlayer.getUniqueId().equals(entity.getUniqueID())) {
      flintEntity = this.clientPlayer;
    }
    if (flintEntity == null) {
      return;
    }
    for (ModelBoxHolder<Entity, EntityRenderContext> modelBoxHolder :
        flintEntity.getRenderContext().getRenderables().values()) {
      modelBoxHolder.callPropertyPreparations().callPropertyHandler();
    }
    this.lastRenderedEntity = flintEntity;
  }
}
