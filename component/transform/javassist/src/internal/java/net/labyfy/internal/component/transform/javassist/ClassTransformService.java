package net.labyfy.internal.component.transform.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.transform.exceptions.ClassTransformException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

@Singleton
@MinecraftTransformer
@Service(ClassTransform.class)
@Deprecated
public class ClassTransformService implements ServiceHandler, LateInjectedTransformer {

  private final Logger logger;
  private final String version;
  private final ClassMappingProvider classMappingProvider;
  private final InternalClassTransformContext.Factory classTransformContextFactory;
  private final Collection<ClassTransformContext> classTransformContexts;
  private final Collection<String> ignoredPackages =
      Arrays.asList("com.mojang.realmsclient", "net.minecraft.realms");

  @Inject
  private ClassTransformService(
      @InjectLogger Logger logger,
      ClassMappingProvider classMappingProvider,
      InternalClassTransformContext.Factory classTransformContextFactory,
      @Named("launchArguments") Map launchArguments) {
    this.logger = logger;
    this.classMappingProvider = classMappingProvider;
    this.classTransformContextFactory = classTransformContextFactory;
    this.classTransformContexts = new HashSet<>();
    this.version = (String) launchArguments.get("--version");
  }

  @Override
  public void discover(Identifier.Base property) {
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

            try {
              return annotation
                  .value()
                  .test(
                      ctClass,
                      InjectionHolder.getInjectedInstance(annotation.classNameResolver())
                          .resolve(annotation.className()));
            } catch (NotFoundException exception) {
              logger.error("Exception while discovering service: {}", annotation.className(), exception);
            }

            return false;
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
  }

  @Override
  public byte[] transform(String className, byte[] bytes) throws ClassTransformException {
    ClassPool classPool = ClassPool.getDefault();
    CtClass ctClass;

    try {
      ctClass = classPool.makeClass(
          new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), true);
    } catch (IOException exception) {
      throw new ClassTransformException("unable to read class", exception);
    }

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
          Method method = classTransformContext.getOwnerMethod();

          try {
            method.invoke(classTransformContext.getOwner(), classTransformContext);
          } catch (IllegalAccessException exception) {
            throw new ClassTransformException("unable to access method: " + method.getName(), exception);
          } catch (InvocationTargetException exception) {
            throw new ClassTransformException(method.getName() + " threw an exception", exception);
          }
        }
      }
    }

    try {
      return ctClass.toBytecode();
    } catch (IOException exception) {
      // Basically unreachable.
      throw new ClassTransformException("unable to write class bytecode to byte array: " + className, exception);
    } catch (CannotCompileException exception) {
      throw new ClassTransformException("unable to transform class: " + className, exception);
    }
  }
}
