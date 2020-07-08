package net.labyfy.internal.component.packages;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.labyfy.component.commons.consumer.TriConsumer;
import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageClassLoader;
import net.labyfy.component.packages.PackageManifest;
import net.labyfy.component.packages.PackageState;
import net.labyfy.component.processing.autoload.AutoLoadProvider;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.internal.component.inject.InjectionServiceShare;
import net.labyfy.internal.component.stereotype.service.ServiceRepository;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarFile;

/**
 * Default implementation of the {@link Package}.
 */
public class DefaultPackage implements Package {
  private final File jarFile;
  private PackageManifest packageManifest;
  private PackageState packageState;
  private PackageClassLoader classLoader;
  private Exception loadException;

  /**
   * Creates a new Labyfy package with the given description loader and the
   * given files.
   *
   * @param manifestLoader The loader to use for reading the manifest
   * @param jarFile        The java IO file this package should be loaded from, or null if loaded from the classpath
   * @param jar            The java IO jar file this package should be loaded from, must point to the same file as
   *                       the `file` parameter, or must be null if the package has been loaded from the classpath
   */
  protected DefaultPackage(
      DefaultPackageManifestLoader manifestLoader,
      File jarFile,
      JarFile jar) {
    this.jarFile = jarFile;

    if (jar != null) {
      // If the package should be loaded from a jar file, try to retrieve the manifest from it
      if (!manifestLoader.isManifestPresent(jar)) {
        throw new IllegalArgumentException("The given JAR file " + jarFile.getName() +
            " does not contain a package manifest");
      }

      // Try to load the manifest
      this.packageState = PackageState.INVALID_MANIFEST;
      PackageManifest manifest;

      try {
        manifest = manifestLoader.loadManifest(jar);

        if (manifest.isValid()) {
          this.packageState = PackageState.NOT_LOADED;
        }

        this.packageManifest = manifest;
      } catch (IOException ignore) {
      }
    } else {
      // The package should not be loaded from a file, thus we don't have a manifest and mark  the package
      // as ready for load
      this.packageState = PackageState.NOT_LOADED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageManifest getPackageManifest() {
    return this.packageManifest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.packageManifest != null ? this.packageManifest.getName() : jarFile.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName() {
    return this.packageManifest != null
        ? this.packageManifest.getDisplayName()
        : jarFile.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return this.packageManifest != null ? this.packageManifest.getVersion() : "unknown";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageState getState() {
    return this.packageState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getFile() {
    return this.jarFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setState(PackageState state) {
    if (packageState != PackageState.NOT_LOADED) {
      throw new IllegalStateException(
          "The package state can only be changed if the package has not been loaded already");
    } else if (state == PackageState.LOADED) {
      throw new IllegalArgumentException("The package state can't be set to LOADED explicitly, use the load() method");
    }

    this.packageState = state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageState load() {
    if (packageState != PackageState.NOT_LOADED) {
      throw new IllegalStateException("The package has to be in the NOT_LOADED state in order to be loaded");
    }

    try {
      this.classLoader = new DefaultPackageClassLoader(this);
      this.packageState = PackageState.LOADED;
    } catch (Exception e) {
      // The package failed to load, save the error and mark the package itself as errored
      this.packageState = PackageState.ERRORED;
      this.loadException = e;
    }
    return this.packageState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enable() {
    if (packageState != PackageState.LOADED) {
      throw new IllegalStateException("The package has to be in the LOADED state in order to be enabled");
    }

    // Find all autoload providers within the package
    Set<AutoLoadProvider> autoLoadProviders =
        ExtendedServiceLoader.get(AutoLoadProvider.class).discover(classLoader.asClassLoader());

    Map<Integer, Multimap<Integer, String>> sortedClasses = new TreeMap<>(Integer::compare);

    TriConsumer<Integer, Integer, String> classAcceptor = (round, priority, name) ->
        sortedClasses
            .computeIfAbsent(round, (k) -> MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build())
            .put(priority, name);

    // Build the map of classes to load
    autoLoadProviders.iterator().forEachRemaining((provider) -> provider.registerAutoLoad(classAcceptor));

    // Iterate over the classes and register them to the service register
    sortedClasses.forEach((round, classes) -> {
      classes.forEach((priority, className) -> {
        try {
          EntryPoint.notifyService(Class.forName(className, true, DefaultPackage.class.getClassLoader()));
        } catch (Exception exception) {
          throw new RuntimeException("Unreachable condition hit: already loaded class not found: " + className, exception);
        }
      });

      InjectionServiceShare.flush();

      try {
        InjectionHolder.getInjectedInstance(ServiceRepository.class).flushAll();
      } catch (ServiceNotFoundException exception) {
        throw new RuntimeException("Unable to discover service during flushAll: " + ServiceRepository.class.getName(), exception);
      }
    });

    // The package is now enabled
    this.packageState = PackageState.ENABLED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageClassLoader getPackageClassLoader() {
    if (packageState != PackageState.LOADED && packageState != PackageState.ENABLED) {
      throw new IllegalStateException("The package has to be in the LOADED or ENABLED state in order to retrieve " +
          "the class loader of it");
    }

    return this.classLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Exception getLoadException() {
    if (packageState != PackageState.ERRORED) {
      throw new IllegalStateException("The package has to be in the ERRORED state in order to retrieve the exception " +
          "which caused its loading to fail");
    }

    return this.loadException;
  }
}
