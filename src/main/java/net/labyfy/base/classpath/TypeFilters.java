package net.labyfy.base.classpath;

import com.google.inject.internal.Annotations;
import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.function.Consumer;

public final class TypeFilters {

  private TypeFilters() {}

  public static TypeFilter log(Consumer<Class> consumer) {
    return clazz -> {
      consumer.accept(clazz);
      return true;
    };
  }

  public static TypeFilter annotatedWith(Annotation... annotations) {
    return clazz ->
        Arrays.stream(annotations)
            .allMatch(
                annotation ->
                    clazz.isAnnotationPresent(annotation.annotationType())
                        && AnnotationUtils.equals(
                            annotation, clazz.getDeclaredAnnotation(annotation.annotationType())));
  }

  public static TypeFilter annotatedWith(Class<? extends Annotation> annotation) {
    return clazz -> clazz.isAnnotationPresent(annotation);
  }

  public static TypeFilter subclassOf(Class<?> superclass) {
    return clazz -> !superclass.equals(clazz) && superclass.isAssignableFrom(clazz);
  }

  public static TypeFilter matchesPackage(String regex) {
    return clazz -> clazz.getPackage().getName().matches(regex);
  }

  /*public static TypeFilter matchesAnyMethod(MethodFilter... methodFilters) {
    return clazz ->
        Arrays.stream(clazz.getDeclaredMethods())
            .anyMatch(
                method ->
                    Arrays.stream(methodFilters)
                        .allMatch(methodFilter -> methodFilter.matches(method)));
  }*/
}
