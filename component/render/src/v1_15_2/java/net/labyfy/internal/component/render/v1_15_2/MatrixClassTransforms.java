package net.labyfy.internal.component.render.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Modifier;
import java.util.Objects;

@Singleton
public class MatrixClassTransforms {
  private final ClassMappingProvider mappingProvider;

  @Inject
  private MatrixClassTransforms(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform(
      value = "net.minecraft.client.renderer.Matrix4f",
      version = "1.15.2"
  )
  public void transform4f(ClassTransformContext ctx) throws NotFoundException, CannotCompileException {
    ClassMapping matrixMapping = mappingProvider.get("net.minecraft.client.renderer.Matrix4f");

    CtClass clazz = ctx.getCtClass();
    clazz.setModifiers(clazz.getModifiers() & ~Modifier.FINAL);

    clazz.setInterfaces(new CtClass[]{clazz.getClassPool().get("net.labyfy.internal.component.render.HookedMatrix4f")});

    String[] names = new String[]{
        "m00",
        "m01",
        "m02",
        "m03",
        "m10",
        "m11",
        "m12",
        "m13",
        "m20",
        "m21",
        "m22",
        "m23",
        "m30",
        "m31",
        "m32",
        "m33"
    };
    for (String field : names) {
      ctx.addMethod("public net.labyfy.internal.component.render.HookedMatrix4f " + field + "(float v) {" +
          " this." + Objects.requireNonNull(matrixMapping.getField(field)).getName() + " = v; return this; }");
      ctx.addMethod("public float " + field + "(){return this." + Objects.requireNonNull(matrixMapping.getField(field)).getName() + ";}");
    }
  }


  @ClassTransform(
      value = "net.minecraft.client.renderer.Matrix3f",
      version = "1.15.2"
  )
  public void transform3f(ClassTransformContext ctx) throws NotFoundException, CannotCompileException {
    ClassMapping matrixMapping = mappingProvider.get("net.minecraft.client.renderer.Matrix3f");

    CtClass clazz = ctx.getCtClass();
    clazz.setModifiers(clazz.getModifiers() & ~Modifier.FINAL);

    clazz.setInterfaces(new CtClass[]{clazz.getClassPool().get("net.labyfy.internal.component.render.HookedMatrix3f")});

    String[] names = new String[]{
        "m00",
        "m01",
        "m02",
        "m10",
        "m11",
        "m12",
        "m20",
        "m21",
        "m22"
    };
    for (String field : names) {
      ctx.addMethod("public net.labyfy.internal.component.render.HookedMatrix3f " + field + "(float v) {" +
          " this." + Objects.requireNonNull(matrixMapping.getField(field)).getName() + " = v; return this; }");
      ctx.addMethod("public float " + field + "(){return this." + Objects.requireNonNull(matrixMapping.getField(field)).getName() + ";}");
    }
  }
}
