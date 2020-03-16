package net.labyfy.component.initializer.inject;

import com.google.common.base.Preconditions;
import com.google.inject.*;
import com.google.inject.name.Names;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class InitializationModule extends AbstractModule {

  private final AtomicReference<Injector> injectorHolder;
  private final Map<String, String> launchArguments;

  private InitializationModule(
          AtomicReference<Injector> injectorHolder,
          Map<String, String> launchArguments) {
    this.injectorHolder = injectorHolder;
    this.launchArguments = launchArguments;
  }

  protected void configure() {
    this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(this.launchArguments);
    this.bind(Key.get(AtomicReference.class, Names.named("injectorReference")))
        .toInstance(this.injectorHolder);

//    LabyVersionProvider labyVersionProvider = LabyVersionProvider.create(launchArguments);
//    this.bind(LabyVersion.class).toProvider(labyVersionProvider::getMinecraftVersion);
//    TypeSearcher typeSearcher = TypeSearcher.create(labyVersionProvider.getMinecraftVersion(), Launch.classLoader);
//    typeSearcher.getSearchingPackages().addAll(this.searchingPackages);
//
//    this.bind(TypeSearcher.class).toInstance(typeSearcher);
//    this.bind(LabyVersionProvider.class).toInstance(labyVersionProvider);
  }



  public static InitializationModule create(
          AtomicReference<Injector> injectorHolder,
          Map<String, String> launchArguments) {
    Preconditions.checkNotNull(injectorHolder);
    Preconditions.checkNotNull(launchArguments);
    return new InitializationModule(injectorHolder, launchArguments);
  }
}
