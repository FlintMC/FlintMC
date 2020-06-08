package net.labyfy.component.initializer;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.base.structure.AutoLoadProvider;
import net.labyfy.base.structure.util.TriConsumer;
import net.labyfy.component.inject.ServiceRepository;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.service.LabyfyServiceLoader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Initializer {

  public static void boot() throws IOException {

    Collection<Method> initializationMethods = new HashSet<>();

    Set<AutoLoadProvider> autoLoadProviders =
        LabyfyServiceLoader.get(AutoLoadProvider.class).discover(LaunchController.getInstance().getRootLoader());

    Map<Integer, Multimap<Integer, String>> sortedClasses = new TreeMap<>(Integer::compare);

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) -> {
      sortedClasses.putIfAbsent(round, MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build());
      sortedClasses.get(round).put(priority, name);
    };

    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classAcceptor));

    sortedClasses.forEach((round, classes) -> {
      classes.forEach((priority, className) -> {
        try {
          EntryPoint.notifyService(Class.forName(className, true, Initializer.class.getClassLoader()));

          // LaunchController.getInstance().getRootLoader().loadClass(clazz);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Unreachable condition hit: already loaded class not found: " + className);
        }
      });
      InjectionServiceShare.flush();
      InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();
    });

  }
}