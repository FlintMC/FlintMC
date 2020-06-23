package net.labyfy.component.packages.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.component.processing.autoload.AutoLoadProvider;
import net.labyfy.component.commons.consumer.TriConsumer;
import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.*;
import net.labyfy.component.service.LabyfyServiceLoader;
import net.labyfy.impl.component.stereotype.service.ServiceRepository;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarFile;

public class LabyPackage implements Package {
  private final File jarFile;
  private PackageDescription packageDescription;
  private PackageState packageState;
  private PackageClassLoader classLoader;
  private Exception loadException;

  protected LabyPackage(
      LabyPackageDescriptionLoader descriptionLoader,
      File jarFile,
      JarFile jar) {
    Preconditions.checkNotNull(jarFile);
    Preconditions.checkArgument(
        descriptionLoader.isDescriptionPresent(jar),
        "The given JAR File (%s) does not contain a valid package description.",
        jarFile.getName());

    this.jarFile = jarFile;
    Optional<PackageDescription> optionalDescription = descriptionLoader.loadDescription(jar);
    if (optionalDescription.isPresent() && optionalDescription.get().isValid()) {
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
      this.classLoader = new LabyPackageClassLoader(this);

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

    Map<Integer, Multimap<Integer, String>> sortedClasses = new TreeMap<>(Integer::compare);

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) -> {
      sortedClasses.putIfAbsent(round, MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build());
      sortedClasses.get(round).put(priority, name);
    };

    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classAcceptor));

    sortedClasses.forEach((round, classes) -> {
      classes.forEach((priority, className) -> {
        try {
          EntryPoint.notifyService(Class.forName(className, true, LabyPackage.class.getClassLoader()));
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException("Unreachable condition hit: already loaded class not found: " + className);
        }
      });
      InjectionServiceShare.flush();
      InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();
    });

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
