package net.labyfy.component.transform.launchplugin;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.component.commons.consumer.TriConsumer;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.processing.autoload.AutoLoadProvider;
import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.ServiceRepository;
import net.labyfy.component.transform.launchplugin.inject.module.BindConstantModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class LabyfyFrameworkInitializer {


  @Inject
  private LabyfyFrameworkInitializer() {
  }

  public void initialize(Map<String, String> arguments) {
    InjectionHolder.getInstance().addModules(new BindConstantModule(arguments));
    ServiceRepository serviceRepository = InjectionHolder.getInjectedInstance(ServiceRepository.class);

    List<DetectableAnnotationProvider.DetectableAnnotationMeta> annotations = getAnnotations();
    System.out.println();

/*

    for (Multimap<Integer, String> multimap : this.getAutoloadedClasses().values()) {
      try {
        for (String className : multimap.values()) {
          CtClass ctClass = ClassPool.getDefault().get(className);
          serviceRepository.notifyClassLoaded(ctClass);
          serviceRepository.flushAll();
        }
//        InjectionServiceShare.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }*/
  }

  private List<DetectableAnnotationProvider.DetectableAnnotationMeta> getAnnotations() {
    List<DetectableAnnotationProvider.DetectableAnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover = ExtendedServiceLoader.get(DetectableAnnotationProvider.class).discover(LaunchController.getInstance().getRootLoader());
    discover.forEach(detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));
    return annotationMetas;
  }

  private Map<Integer, Multimap<Integer, String>> getAutoloadedClasses() {
    Set<AutoLoadProvider> autoLoadProviders = ExtendedServiceLoader.get(AutoLoadProvider.class).discover(LaunchController.getInstance().getRootLoader());
    Map<Integer, Multimap<Integer, String>> sortedClasses = new TreeMap<>(Integer::compare);

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) -> {
      sortedClasses.putIfAbsent(round, MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build());
      sortedClasses.get(round).put(priority, name);
    };

    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classAcceptor));
    return sortedClasses;
  }
}
