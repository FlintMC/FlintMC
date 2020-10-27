package net.labyfy.internal.component.transform.javassist;

import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.transform.exceptions.ClassTransformException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.MethodBasedClassTransformMeta;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

@Implement(MethodBasedClassTransformMeta.class)
public class InternalMethodBasedClassTransformMeta implements MethodBasedClassTransformMeta {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final Collection<Predicate<CtClass>> filters;
  private final ClassMappingProvider classMappingProvider;
  private final AnnotationMeta<ClassTransform> annotationMeta;
  private final Logger logger;
  private final String version;
  private final NameResolver classNameResolver;
  private Object transformInstance;

  @AssistedInject
  private InternalMethodBasedClassTransformMeta(
      ClassTransformContext.Factory classTransformContextFactory,
      ClassMappingProvider classMappingProvider,
      @InjectLogger Logger logger,
      @Assisted AnnotationMeta<ClassTransform> annotationMeta,
      @Named("launchArguments") Map launchArguments) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.logger = logger;
    this.version = (String) launchArguments.get("--game-version");
    this.classMappingProvider = classMappingProvider;
    this.annotationMeta = annotationMeta;
    this.classNameResolver = InjectionHolder.getInjectedInstance(getAnnotation().classNameResolver());

    this.filters = this.createFilters();
  }

  private Collection<Predicate<CtClass>> createFilters() {
    Collection<Predicate<CtClass>> filters = new HashSet<>();
    for (AnnotationMeta<CtClassFilter> ctClassFilter :
        this.getAnnotationMeta().getMetaData(CtClassFilter.class)) {
      filters.add(
          ctClass -> {
            CtClassFilter classFilterAnnotation = ctClassFilter.getAnnotation();
            try {
              NameResolver classNameResolver =
                  InjectionHolder.getInjectedInstance(classFilterAnnotation.classNameResolver());

              return classFilterAnnotation
                  .value()
                  .test(ctClass, classNameResolver.resolve(classFilterAnnotation.className()));

            } catch (NotFoundException exception) {
              logger.error(
                  "Exception while discovering service: {}",
                  classFilterAnnotation.className(),
                  exception);
            }
            return false;
          });
    }
    return Collections.unmodifiableCollection(filters);
  }

  @Override
  public void execute(CtClass ctClass) throws ClassTransformException {
    try {
      CtResolver.get(this.getTransformMethod())
          .invoke(this.getTransformInstance(), this.classTransformContextFactory.create(ctClass));
    } catch (IllegalAccessException exception) {
      throw new ClassTransformException(
          "Unable to access method: " + this.getTransformMethod().getName(), exception);
    } catch (InvocationTargetException exception) {
      throw new ClassTransformException(
          this.getTransformMethod().getName() + " threw an exception", exception);
    }
  }

  @Override
  public boolean matches(CtClass ctClass) {
    if (ctClass.equals(this.getTransformClass())) {
      return false;
    }
    for (Predicate<CtClass> filter : this.getFilters()) {
      if (!filter.test(ctClass)) return false;
    }
    ClassMapping classMapping = classMappingProvider.get(ctClass.getName());

    ClassTransform annotation = this.getAnnotationMeta().getAnnotation();
    if (classMapping == null)
      classMapping =
          new ClassMapping(
              InjectionHolder.getInjectedInstance(
                  Key.get(boolean.class, Names.named("obfuscated"))),
              ctClass.getName(),
              ctClass.getName());

    for (String target : annotation.value()) {
      String resolve = this.getClassNameResolver().resolve(target);

      if (resolve != null) {
        target = resolve;
      }

      return (annotation.version().isEmpty() || annotation.version().equals(this.version))
          && ((target.isEmpty() || target.equals(classMapping.getDeobfuscatedName()))
          || target.equals(classMapping.getObfuscatedName()))
          && this.getFilters().stream()
          .allMatch(ctClassPredicate -> ctClassPredicate.test(ctClass));
    }
    return true;
  }

  @Override
  public Collection<Predicate<CtClass>> getFilters() {
    return this.filters;
  }

  @Override
  public CtMethod getTransformMethod() {
    return this.annotationMeta.getMethodIdentifier().getLocation();
  }

  @Override
  public CtClass getTransformClass() {
    return this.getTransformMethod().getDeclaringClass();
  }

  @Override
  public Object getTransformInstance() {
    if (this.transformInstance == null) {
      this.transformInstance =
          InjectionHolder.getInjectedInstance(CtResolver.get(getTransformClass()));
    }
    return this.transformInstance;
  }

  @Override
  public AnnotationMeta<ClassTransform> getAnnotationMeta() {
    return this.annotationMeta;
  }

  @Override
  public ClassTransform getAnnotation() {
    return this.getAnnotationMeta().getAnnotation();
  }

  @Override
  public NameResolver getClassNameResolver() {
    return this.classNameResolver;
  }

  @Override
  public int getPriority() {
    return this.getAnnotation().priority();
  }
}
