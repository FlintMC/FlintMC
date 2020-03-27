package net.labyfy.base.structure.service;

import com.google.common.reflect.ClassPath;
import com.google.inject.Singleton;
import net.labyfy.base.structure.Initialize;
import net.labyfy.base.structure.identifier.IgnoreInitialization;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ServiceInitializer {

  @Initialize
  public static void init() throws IOException, ClassNotFoundException {
    Set<? extends Class<?>> collect =
        ClassPath.from(ServiceInitializer.class.getClassLoader()).getAllClasses().stream()
            .filter(classInfo -> classInfo.getName().startsWith("net.labyfy"))
            .map(ClassPath.ClassInfo::load)
            .collect(Collectors.toSet());

    Collection<Class> classes = new HashSet<>();
    Collection<Class> services = new HashSet<>();

    collect.forEach(
        clazz -> {
          try {
            if (clazz.isAnnotationPresent(IgnoreInitialization.class)) return;
            if (clazz.isAnnotationPresent(Service.class)) {
              services.add(clazz);
            } else {
              classes.add(clazz);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    services.stream()
        .sorted(
            Comparator.comparingInt(
                o -> ((Service) o.getDeclaredAnnotation(Service.class)).priority()))
        .forEach(
            service -> {
              try {
                Class.forName(service.getName(), true, ServiceInitializer.class.getClassLoader());
              } catch (ClassNotFoundException e) {
                e.printStackTrace();
              }
            });

    for (Class<?> clazz : classes) {

      Class.forName(clazz.getName(), true, ServiceInitializer.class.getClassLoader());
    }
  }
}
