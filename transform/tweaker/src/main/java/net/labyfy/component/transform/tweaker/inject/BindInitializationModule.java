package net.labyfy.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.version.VersionProvider;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class BindInitializationModule extends AbstractModule {

  private final Collection<String> searchingPackages;
  private final AtomicReference<Injector> injectorHolder;
  private final Map<String, String> launchArguments;

  private BindInitializationModule(
      Collection<String> searchingPackages,
      AtomicReference<Injector> injectorHolder,
      Map<String, String> launchArguments) {
    this.searchingPackages = searchingPackages;
    this.injectorHolder = injectorHolder;
    this.launchArguments = launchArguments;
  }

  protected void configure() {
    this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(this.launchArguments);
    this.bind(Key.get(AtomicReference.class, Names.named("injectorReference")))
            .toInstance(this.injectorHolder);

    VersionProvider labyVersionProvider = VersionProvider.create(launchArguments);
    System.out.println(this.searchingPackages);
//    TypeSearcher typeSearcher = TypeSearcher.create(labyVersionProvider.getMinecraftVersion(), Launch.classLoader);
//    typeSearcher.getSearchingPackages().addAll(this.searchingPackages);
//
//    this.bind(TypeSearcher.class).toInstance(typeSearcher);
//    this.bind(LabyVersionProvider.class).toInstance(labyVersionProvider);
  }

  public static BindInitializationModule create(
      Collection<String> searchingPackages,
      AtomicReference<Injector> injectorHolder,
      Map<String, String> launchArguments) {
    return new BindInitializationModule(searchingPackages, injectorHolder, launchArguments);
  }
}
