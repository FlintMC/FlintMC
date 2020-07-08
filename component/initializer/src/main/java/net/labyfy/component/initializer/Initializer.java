package net.labyfy.component.initializer;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.component.commons.consumer.TriConsumer;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.processing.autoload.AutoLoadProvider;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.internal.component.inject.InjectionServiceShare;
import net.labyfy.internal.component.stereotype.service.ServiceRepository;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Initializer {
  public static void boot() throws ClassNotFoundException, ServiceNotFoundException {
    Set<AutoLoadProvider> autoLoadProviders =
        ExtendedServiceLoader.get(AutoLoadProvider.class).discover(LaunchController.getInstance().getRootLoader());

    Map<Integer, Multimap<Integer, String>> sortedClasses = new TreeMap<>(Integer::compare);

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) -> {
      sortedClasses.putIfAbsent(round, MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build());
      sortedClasses.get(round).put(priority, name);
    };

    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classAcceptor));

    for (Multimap<Integer, String> classes : sortedClasses.values()) {
      for (String className : classes.values()) {
        EntryPoint.notifyService(Class.forName(className, true, Initializer.class.getClassLoader()));
      }

      InjectionServiceShare.flush();
      InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();
    }
  }
}
