package net.labyfy.component.transform.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
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
import java.util.function.Predicate;

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

      Collection<Predicate<CtClass>> filters = new HashSet<>();

      for (Property.Base subProperty :
          property.getProperty().getSubProperties(CtClassFilter.class)) {
        filters.add(
            ctClass -> {
              CtClassFilter annotation =
                  subProperty.getLocatedIdentifiedAnnotation().getAnnotation();
              return annotation
                  .value()
                  .test(
                      ctClass,
                      InjectionHolder.getInstance()
                          .getInjector()
                          .getInstance(annotation.classNameResolver())
                          .resolve(annotation.className()));
            });
      }

      this.classTransformContexts.add(
          this.classTransformContextFactory.create(
              filters,
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

      for (ClassTransformContext classTransformContext : this.classTransformContexts) {
        for (String target : classTransformContext.getClassTransform().value()) {
          target =
              InjectionHolder.getInstance()
                  .getInjector()
                  .getInstance(classTransformContext.getClassTransform().classNameResolver())
                  .resolve(target);


          if (((target.isEmpty() || target.equals(classMapping.getUnObfuscatedName()))
                  || target.equals(classMapping.getObfuscatedName()))
              && classTransformContext.getFilters().stream()
                  .allMatch(ctClassPredicate -> ctClassPredicate.test(ctClass))) {

            classTransformContext.setCtClass(ctClass);
            classTransformContext
                .getOwnerMethod()
                .invoke(
                    InjectionHolder.getInstance()
                        .getInjector()
                        .getInstance(classTransformContext.getOwner()),
                    classTransformContext);
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
