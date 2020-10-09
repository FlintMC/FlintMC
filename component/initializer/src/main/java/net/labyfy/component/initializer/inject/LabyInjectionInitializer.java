package net.labyfy.component.initializer.inject;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.Map;

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
    this.createInjector();
  }


  private void createInjector() {
  }
}
