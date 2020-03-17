package net.labyfy.component.transform.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

@Singleton
@MinecraftTransformer
@Service(ClassTransform.class)
public class ClassTransformService implements ServiceHandler, IClassTransformer {

  private final ClassMappingProvider classMappingProvider;
  private final ClassTransformContext.Factory classTransformContextFactory;
  private final Collection<ClassTransformContext> classTransformContexts;

  @Inject
  private ClassTransformService(
      ClassMappingProvider classMappingProvider,
      ClassTransformContext.Factory classTransformContextFactory) {
    this.classMappingProvider = classMappingProvider;
    this.classTransformContextFactory = classTransformContextFactory;
    this.classTransformContexts = new HashSet<>();
  }

  public synchronized void discover(Identifier.Base property) {
    try {
      LocatedIdentifiedAnnotation locatedIdentifiedAnnotation =
          property.getProperty().getLocatedIdentifiedAnnotation();
      Launch.classLoader.addTransformerExclusion(
          locatedIdentifiedAnnotation.<Method>getLocation().getDeclaringClass().getName());

      this.classTransformContexts.add(
          this.classTransformContextFactory.create(
              locatedIdentifiedAnnotation.getAnnotation(),
              locatedIdentifiedAnnotation.getLocation(),
              locatedIdentifiedAnnotation.<Method>getLocation().getDeclaringClass()));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public synchronized byte[] transform(
      String className, String transformedClassName, byte[] bytes) {
    try {
      ClassPool classPool = ClassPool.getDefault();
      CtClass ctClass =
          classPool.makeClass(
              new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), false);

      ClassMapping classMapping = classMappingProvider.get(className);

      if (classMapping == null)
        classMapping = ClassMapping.create(classMappingProvider, className, transformedClassName);

      for (ClassTransformContext classTransformer : this.classTransformContexts) {
        for (String target : classTransformer.getClassTransform().value()) {

          if ((target.isEmpty() || target.equals(classMapping.getUnObfuscatedName()))
              || target.equals(classMapping.getObfuscatedName())) {
            classTransformer.setCtClass(ctClass);
            classTransformer
                .getOwnerMethod()
                .invoke(
                    InjectionHolder.getInstance()
                        .getInjector()
                        .getInstance(classTransformer.getOwner()),
                    classTransformer);
          }
        }
      }

      return ctClass.toBytecode();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return bytes;
  }
}
