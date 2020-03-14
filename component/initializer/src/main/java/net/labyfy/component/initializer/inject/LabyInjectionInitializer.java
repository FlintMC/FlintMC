package net.labyfy.component.initializer.inject;

import com.google.inject.*;
import com.google.inject.name.Named;
import net.labyfy.component.initializer.inject.module.BindConstantModule;
import net.labyfy.component.initializer.inject.module.PostConstructModule;
import net.labyfy.inject.InjectionHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class LabyInjectionInitializer {

  private final Injector injector;
  private final Map<String, String> launchArguments;

  @Inject
  private LabyInjectionInitializer(Injector injector, @Named("launchArguments") Map launchArguments) {
    this.injector = injector;
    this.launchArguments = launchArguments;
    this.init();
  }

  private void init() {
    System.out.println("init " + getClass().getClassLoader());
    this.createInjector(Collections.emptyList());

  }

  public Collection<Module> collectModules(
      Collection<String> searchingPackages, AtomicReference<Injector> injectorReference) {
    return Arrays.asList(
        InitializationModule.create(searchingPackages, injectorReference, this.launchArguments),
        this.injector.getInstance(BindConstantModule.class),
        this.injector.getInstance(PostConstructModule.class));
  }

  private void createInjector(Collection<String> searchingPackages) {
    AtomicReference<Injector> injectorReference = new AtomicReference<>();
    Injector injector =
        Guice.createInjector(
            this.collectModules(searchingPackages, injectorReference).toArray(new Module[] {}));
    injectorReference.set(injector);
    InjectionHolder.setInjector(injector);
  }
}
