package net.flintmc.mcapi.v1_15_2.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javassist.*;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.internal.entity.cache.EntityCache;
import net.flintmc.render.model.ModelBox;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.CtClassFilters;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class ModelRendererInterceptor {

  private static ModelRendererInterceptor INSTANCE;
  private final ClassMappingProvider classMappingProvider;
  private final EntityCache entityCache;
  private Entity lastRenderedEntity;

  @Inject
  private ModelRendererInterceptor(
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

  public static void interceptRotationAnglesUpdate(Object entity) {
    INSTANCE.rotationAnglesUpdate(entity);
  }

  @ClassTransform("net.minecraft.client.renderer.model.ModelRenderer")
  public void transform(ClassTransformContext classTransformContext) {
    try {
      CtClass[] parameters =
          ClassPool.getDefault()
              .get(
                  new String[]{
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
      render.insertBefore(
          "{if(net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.interceptRender($0, $$)){return;}}");
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
    if (flintEntity == null) return;
    for (ModelBox modelBox : flintEntity.getRenderContext().getRenderables().values()) {
      modelBox.callPropertyHandler();
    }
    this.lastRenderedEntity = flintEntity;
  }

  @SuppressWarnings("unchecked")
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
    ModelBox modelBox = lastRenderedEntity.getRenderContext().getRenderableByTarget(instance);
    if (modelBox == null) return false;
    modelBox.callRenderPreparations();
    if (modelBox.getContext().getRenderer() != null) {
      modelBox.getContext().getRenderer().render(modelBox);
      return true;
    } else {
      return false;
    }
  }
}
