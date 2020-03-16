package net.labyfy.component.initializer.inject;

import com.google.inject.*;
import com.google.inject.name.Named;
import net.labyfy.component.initializer.inject.module.BindConstantModule;
import net.labyfy.component.initializer.inject.module.PostConstructModule;
import net.labyfy.component.inject.InjectionHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class LabyInjectionInitializer {

  private final Injector injector;
  private final Map<String, String> launchArguments;

  @Inject
  private LabyInjectionInitializer(
      Injector injector, @Named("launchArguments") Map launchArguments) {
    this.injector = injector;
    this.launchArguments = launchArguments;
    this.init();
  }

  private void init() {
    System.out.println("init " + getClass().getClassLoader());
    this.createInjector();
  }

  public Collection<Module> collectModules(
          AtomicReference<Injector> injectorReference) {
    return Arrays.asList(
        this.injector.getInstance(BindConstantModule.class),
        this.injector.getInstance(PostConstructModule.class));
  }

  private void createInjector() {
    InjectionHolder.getInstance()
        .addModules(
            this.collectModules(
                    InjectionHolder.getInstance().getInjectorReference())
                .toArray(new Module[] {}));
    InjectionHolder.getInstance().getInjector();
  }
}
