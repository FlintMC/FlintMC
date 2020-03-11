package net.labyfy.structure.annotation;

import com.google.common.collect.Sets;
import net.labyfy.structure.annotation.Transitive;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;

@Singleton
public class AnnotationCollector {

  @Inject
  private AnnotationCollector() {}

  public Collection<Annotation> getTransitiveAnnotations(Class<?> clazz) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
      if (this.getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  public Collection<Annotation> getTransitiveAnnotations(Method method) {
    Collection<Annotation> annotations = Sets.newHashSet();
    for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
      if (this.getRealAnnotationClass(declaredAnnotation).isAnnotationPresent(Transitive.class))
        annotations.add(declaredAnnotation);
    }
    return annotations;
  }

  public Class<? extends Annotation> getRealAnnotationClass(Annotation annotation) {
    Class<? extends Annotation> annotationClass;
    if (annotation instanceof Proxy) {
      annotationClass = (Class<? extends Annotation>) annotation.getClass().getInterfaces()[0];
    } else {
      annotationClass = annotation.getClass();
    }
    return annotationClass;
  }
}
