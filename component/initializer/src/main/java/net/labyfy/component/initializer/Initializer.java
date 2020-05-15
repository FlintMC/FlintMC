package net.labyfy.component.initializer;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.base.structure.AutoLoadProvider;
import net.labyfy.component.inject.ServiceRepository;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.service.LabyfyServiceLoader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class Initializer {

  public static void boot() throws IOException {
    Collection<Method> initializationMethods = new HashSet<>();

    Set<AutoLoadProvider> autoLoadProviders =
        LabyfyServiceLoader.get(AutoLoadProvider.class).discover(LaunchController.getInstance().getRootLoader());

    //    Set<Class<?>> collect =
    //        ClassPath.from(Initializer.class.getClassLoader()).getAllClasses().stream()
    //            .filter(classInfo -> classInfo.getName().startsWith("net.labyfy"))
    //            .map(ClassPath.ClassInfo::load)
    //            .collect(Collectors.toSet());

    Multimap<Integer, Class<?>> classes =
        MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build();

    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classes));

    classes
        .values()
        .forEach(
            clazz -> {
              try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
              } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unreachable condition hit: Already loaded class not found");
              }
            });

    InjectionServiceShare.flush();
    InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();

    //    for (Class<?> aClass : collect) {Gu
    //      for (Method declaredMethod : aClass.getDeclaredMethods()) {
    //        for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
    //          if (Initialize.class.isAssignableFrom(declaredAnnotation.getClass())) {
    //            initializationMethods.add(declaredMethod);
    //          }
    //        }
    //      }
    //    }
    //
    //    initializationMethods.stream()
    //        .sorted(Comparator.comparingInt(o ->
    // o.getDeclaredAnnotation(Initialize.class).priority()))
    //        .forEach(
    //            method -> {
    //              try {
    //                method.invoke(null);
    //              } catch (IllegalAccessException | InvocationTargetException e) {
    //                e.printStackTrace();
    //              }
    //            });
  }
}
