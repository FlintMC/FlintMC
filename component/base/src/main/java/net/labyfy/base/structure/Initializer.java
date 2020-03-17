package net.labyfy.base.structure;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Initializer {

  public static void boot() throws IOException {
    Collection<Method> initializationMethods = new HashSet<>();

    Set<? extends Class<?>> collect =
        ClassPath.from(Initializer.class.getClassLoader()).getAllClasses().stream()
            .filter(classInfo -> classInfo.getName().startsWith("net.labyfy"))
            .map(ClassPath.ClassInfo::load)
            .collect(Collectors.toSet());

    for (Class<?> aClass : collect) {
      for (Method declaredMethod : aClass.getDeclaredMethods()) {
        for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
          if (Initialize.class.isAssignableFrom(declaredAnnotation.getClass())) {
            initializationMethods.add(declaredMethod);
          }
        }
      }
    }

    initializationMethods.stream()
        .sorted(Comparator.comparingInt(o -> o.getDeclaredAnnotation(Initialize.class).priority()))
        .forEach(
            method -> {
              try {
                method.invoke(null);
              } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
              }
            });
  }
}
