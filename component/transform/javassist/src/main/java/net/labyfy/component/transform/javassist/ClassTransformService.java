package net.labyfy.component.transform.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.bytecode.ClassFile;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

@Singleton
@MinecraftTransformer
@Service(ClassTransform.class)
public class ClassTransformService implements ServiceHandler, LateInjectedTransformer {

  private final String version;
  private final ClassMappingProvider classMappingProvider;
  private final ClassTransformContext.Factory classTransformContextFactory;
  private final Collection<ClassTransformContext> classTransformContexts;
  private final Collection<String> ignoredPackages =
      Arrays.asList("com.mojang.realmsclient", "net.minecraft.realms");

  @Inject
  private ClassTransformService(
          ClassMappingProvider classMappingProvider,
          ClassTransformContext.Factory classTransformContextFactory,
          @Named("launchArguments") Map launchArguments) {
    this.classMappingProvider = classMappingProvider;
    this.classTransformContextFactory = classTransformContextFactory;
    this.classTransformContexts = new HashSet<>();
    this.version = (String) launchArguments.get("--version");
  }

  public void discover(Identifier.Base property) {
    try {
      LocatedIdentifiedAnnotation locatedIdentifiedAnnotation =
              property.getProperty().getLocatedIdentifiedAnnotation();
      LaunchController.getInstance().getRootLoader().excludeFromModification(
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
                                  InjectionHolder.getInjectedInstance(annotation.classNameResolver())
                                          .resolve(annotation.className()));
            });
      }

      this.classTransformContexts.add(
              this.classTransformContextFactory.create(
                      filters,
                      InjectionHolder.getInjectedInstance(
                              locatedIdentifiedAnnotation.<ClassTransform>getAnnotation().classNameResolver()),
                      locatedIdentifiedAnnotation.getAnnotation(),
                      locatedIdentifiedAnnotation.getLocation(),
                      locatedIdentifiedAnnotation.<Method>getLocation().getDeclaringClass(),
                      InjectionHolder.getInjectedInstance(
                              locatedIdentifiedAnnotation.<Method>getLocation().getDeclaringClass())));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public byte[] transform(String className, byte[] bytes) {
    try {
      ClassPool classPool = ClassPool.getDefault();
      CtClass ctClass =
              classPool.makeClass(
                      new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), true);

      ClassMapping classMapping = classMappingProvider.get(className);

      if (classMapping == null)
        classMapping = ClassMapping.create(classMappingProvider, className, className);

      for (String ignoredPackage : this.ignoredPackages) {
        if (classMapping.getUnObfuscatedName().startsWith(ignoredPackage)) {
          return bytes;
        }
      }

      for (ClassTransformContext classTransformContext : this.classTransformContexts) {
        for (String target : classTransformContext.getClassTransform().value()) {
          target = classTransformContext.getNameResolver().resolve(target);

          if ((classTransformContext.getClassTransform().version().isEmpty()
                  || classTransformContext.getClassTransform().version().equals(this.version))
              && ((target.isEmpty() || target.equals(classMapping.getUnObfuscatedName()))
                  || target.equals(classMapping.getObfuscatedName()))
              && classTransformContext.getFilters().stream()
                  .allMatch(ctClassPredicate -> ctClassPredicate.test(ctClass))) {

            classTransformContext.setCtClass(ctClass);
            classTransformContext
                    .getOwnerMethod()
                    .invoke(classTransformContext.getOwner(), classTransformContext);
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
