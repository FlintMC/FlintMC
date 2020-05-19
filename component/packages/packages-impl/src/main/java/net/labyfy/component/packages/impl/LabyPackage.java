package net.labyfy.component.packages.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.base.structure.AutoLoadProvider;
import net.labyfy.base.structure.util.TriConsumer;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.inject.ServiceRepository;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.*;
import net.labyfy.component.service.LabyfyServiceLoader;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;

@Implement(Package.class)
public class LabyPackage implements Package {
  private final File jarFile;
  private PackageDescription packageDescription;
  private PackageClassLoader.Factory classLoaderFactory;
  private PackageState packageState;
  private PackageClassLoader classLoader;
  private Exception loadException;

  @AssistedInject
  private LabyPackage(
      PackageDescriptionLoader descriptionLoader,
      PackageClassLoader.Factory classLoaderFactory,
      @Assisted File jarFile,
      @Assisted JarFile jar) {
    Preconditions.checkNotNull(jarFile);
    Preconditions.checkArgument(
        descriptionLoader.isDescriptionPresent(jar),
        "The given JAR File (%s) does not contain a valid package description.",
        jarFile.getName());

    this.jarFile = jarFile;
    Optional<PackageDescription> optionalDescription = descriptionLoader.loadDescription(jar);
    if (optionalDescription.isPresent() && optionalDescription.get().isValid()) {
      this.classLoaderFactory = classLoaderFactory;
      this.packageState = PackageState.NOT_LOADED;
    } else {
      this.packageState = PackageState.INVALID_DESCRIPTION;
    }
    optionalDescription.ifPresent(description -> this.packageDescription = description);
    this.loadException = null;
  }

  @Override
  public PackageDescription getPackageDescription() {
    return this.packageDescription;
  }

  @Override
  public String getName() {
    return this.packageDescription != null ? this.packageDescription.getName() : jarFile.getName();
  }

  @Override
  public String getDisplayName() {
    return this.packageDescription != null
        ? this.packageDescription.getDisplayName()
        : jarFile.getName();
  }

  @Override
  public String getVersion() {
    return this.packageDescription != null ? this.packageDescription.getVersion() : "unknown";
  }

  @Override
  public PackageState getState() {
    return this.packageState;
  }

  @Override
  public File getFile() {
    return this.jarFile;
  }

  @Override
  public void setState(PackageState state) {
    Preconditions.checkState(this.packageState.equals(PackageState.NOT_LOADED));
    Preconditions.checkArgument(
        !state.equals(PackageState.LOADED),
        "The package state can't be explicitly set to LOADED. "
            + "To get into the LOADED state, you must call the load() method.");

    this.packageState = state;
  }

  @Override
  public PackageState load() {
    Preconditions.checkState(
        PackageState.NOT_LOADED.matches(this),
        "The package must be in NOT_LOADED state to be loaded.");

    try {
      this.classLoader = this.classLoaderFactory.create(this);

      this.packageState = PackageState.LOADED;
    } catch (Exception e) {
      this.packageState = PackageState.ERRORED;
      this.loadException = e;
    }
    return this.packageState;
  }

  @Override
  public void enable() {
    Preconditions.checkState(PackageState.LOADED.matches(this));

    Set<AutoLoadProvider> autoLoadProviders =
        LabyfyServiceLoader.get(AutoLoadProvider.class).discover(classLoader.asClassLoader());

    Map<Integer, Multimap<Integer, String>> sortedClasses = new ConcurrentHashMap<>();

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) -> {
      sortedClasses.putIfAbsent(round, MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build());
      sortedClasses.get(round).put(priority, name);
    };

    autoLoadProviders.forEach((provider) -> provider.registerAutoLoad(classAcceptor));

    sortedClasses.forEach((round, classes) -> {
      classes.forEach((priority, className) -> {
        try {
          Class.forName(className, true, LabyPackage.class.getClassLoader());
          // LaunchController.getInstance().getRootLoader().loadClass(clazz);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Unreachable condition hit: already loaded class not found: " + className);
        }
      });
    });
//    classes
//        .values()
//        .stream().flatMap(integerStringMultimap -> integerStringMultimap.values().stream())
//        .forEach(
//            clazz -> {
//              try {
//                Class.forName(clazz, true, LabyPackage.class.getClassLoader());
//              } catch (ClassNotFoundException e) {
//                throw new RuntimeException("Unreachable condition hit: already loaded class not found: " + clazz);
//              }
//            });

    InjectionServiceShare.flush();
    InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();

    this.packageState = PackageState.ENABLED;
  }

  @Override
  public PackageClassLoader getPackageClassLoader() {
    Preconditions.checkState(
        PackageState.LOADED.matches(this) || PackageState.ENABLED.matches(this));

    return this.classLoader;
  }

  @Override
  public Exception getLoadException() {
    Preconditions.checkState(this.packageState.equals(PackageState.ERRORED));
    Preconditions.checkNotNull(this.loadException);
    return this.loadException;
  }
}
