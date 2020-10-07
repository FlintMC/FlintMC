package net.labyfy.internal.component.transform.javassist;

import com.google.inject.Key;
import com.google.inject.name.Names;
import javassist.*;
import javassist.bytecode.ClassFile;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

@Singleton
@MinecraftTransformer
@Service(ClassTransform.class)
@Deprecated
public class ClassTransformService implements ServiceHandler<ClassTransform>, LateInjectedTransformer {

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
    this.version = (String) launchArguments.get("--game-version");
  }

  @Override
  public void discover(IdentifierMeta<ClassTransform> identifierMeta) throws ServiceNotFoundException {
    Collection<Predicate<CtClass>> filters = new HashSet<>();
    ClassTransform classTransformAnnotation = identifierMeta.getAnnotation();

    for (IdentifierMeta<CtClassFilter> ctClassFilter : identifierMeta.getProperties(CtClassFilter.class)) {
      filters.add(ctClass -> {
        CtClassFilter classFilterAnnotation = ctClassFilter.getAnnotation();
        try {
          return classFilterAnnotation
              .value()
              .test(
                  ctClass,
                  InjectionHolder.getInjectedInstance(classFilterAnnotation.classNameResolver())
                      .resolve(classFilterAnnotation.className()));
        } catch (NotFoundException exception) {
          logger.error("Exception while discovering service: {}", classFilterAnnotation.className(), exception);
        }

        return false;
      });

    }

    this.classTransformContexts.add(
        this.classTransformContextFactory.create(
            filters,
            InjectionHolder.getInjectedInstance(
                classTransformAnnotation.classNameResolver()),
            classTransformAnnotation,
            identifierMeta.getTarget(),
            identifierMeta.<CtMethod>getTarget().getDeclaringClass()));
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
      classMapping = new ClassMapping(InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated"))), className, className);

    for (String ignoredPackage : this.ignoredPackages) {
      if (classMapping.getDeobfuscatedName().startsWith(ignoredPackage)) {
        return bytes;
      }
    }

    for (ClassTransformContext classTransformContext : this.classTransformContexts) {
      CtClass ownerClass = classTransformContext.getOwnerClass();

      for (String target : classTransformContext.getClassTransform().value()) {
        String resolve = classTransformContext.getNameResolver().resolve(target);

        if (resolve != null) {
          target = resolve;
        }

        if ((classTransformContext.getClassTransform().version().isEmpty()
            || classTransformContext.getClassTransform().version().equals(this.version))
            && ((target.isEmpty() || target.equals(classMapping.getDeobfuscatedName()))
            || target.equals(classMapping.getObfuscatedName()))
            && classTransformContext.getFilters().stream()
            .allMatch(ctClassPredicate -> ctClassPredicate.test(ctClass))) {

          classTransformContext.setCtClass(ctClass);
          CtMethod method = classTransformContext.getOwnerMethod();

          try {
            CtResolver.get(method).invoke(InjectionHolder.getInjectedInstance(CtResolver.get(ownerClass)), classTransformContext);
          } catch (IllegalAccessException exception) {
            throw new ClassTransformException("Unable to access method: " + method.getName(), exception);
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
      throw new ClassTransformException("Unable to write class bytecode to byte array: " + className, exception);
    } catch (CannotCompileException exception) {
      throw new ClassTransformException("Unable to transform class: " + className, exception);
    }
  }
}
