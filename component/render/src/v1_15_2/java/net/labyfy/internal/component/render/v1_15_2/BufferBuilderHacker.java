package net.labyfy.internal.component.render.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@AutoLoad
@Singleton
public class BufferBuilderHacker {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private BufferBuilderHacker(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform("net.minecraft.client.renderer.BufferBuilder")
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    CtClass ctClass = classTransformContext.getCtClass();
    ctClass.addInterface(ctClass.getClassPool().get("net.labyfy.internal.component.render.v1_15_2.BufferBuilderAccessor"));
    ClassMapping classMapping = classMappingProvider.get("net.minecraft.client.renderer.BufferBuilder");
    String byteBuffer = classMapping.getField("byteBuffer").getName();
    ctClass.addMethod(CtMethod.make("public java.nio.ByteBuffer getByteBuffer(){return this." + byteBuffer + ";}", ctClass));
    ctClass.addMethod(CtMethod.make("public void setByteBuffer(java.nio.ByteBuffer buffer){this." + byteBuffer + " = buffer;}}", ctClass));
    ctClass.addMethod(CtMethod.make("public void setVertexCount(int vertexCount){this." + classMapping.getField("vertexCount").getName() + " = vertexCount;}", ctClass));
  }

}
