package net.flintmc.mcapi.v1_15_2.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.render.model.RenderableRepository;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class ModelRendererInterceptor {

  private static ModelRendererInterceptor INSTANCE;
  private final ClassMappingProvider classMappingProvider;
  private final RenderableRepository renderableRepository;

  @Inject
  private ModelRendererInterceptor(ClassMappingProvider classMappingProvider, RenderableRepository renderableRepository) {
    this.classMappingProvider = classMappingProvider;
    this.renderableRepository = renderableRepository;
    INSTANCE = this;
  }

  @ClassTransform("net.minecraft.client.renderer.model.ModelRenderer")
  public void transform(ClassTransformContext classTransformContext) {
    try {
      CtClass[] parameters = ClassPool.getDefault().get(new String[]{
          this.classMappingProvider.get("com.mojang.blaze3d.matrix.MatrixStack").getName(),
          this.classMappingProvider.get("com.mojang.blaze3d.vertex.IVertexBuilder").getName(),
          "int",
          "int",
          "float",
          "float",
          "float",
          "float"
      });

      classTransformContext.getCtClass().getDeclaredMethod(
          classMappingProvider
              .get("net.minecraft.client.renderer.model.ModelRenderer")
              .getMethod("render", parameters)
              .getName(), parameters)
          .setBody("net.flintmc.mcapi.v1_15_2.entity.render.ModelRendererInterceptor.intercept($0, $$);");
    } catch (NotFoundException | CannotCompileException e) {
      e.printStackTrace();
    }
  }


  public static void intercept(Object instance, MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    INSTANCE.render(instance, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
  }

  public void render(Object instance, MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    this.renderableRepository.render(instance);
  }

}
