package net.labyfy.component.packages;

import java.util.Set;

/** Represents metadata about a package. */
public interface PackageDescription {

  /** @return the name of the package. */
  String getName();

  /** @return the name of the package as it should be displayed to the user. */
  String getDisplayName();

  /** @return the version of the package. */
  String getVersion();

  /** @return a list of authors that wrote the package. */
  Set<String> getAuthors();

  /** @return a short description of the package. */
  String getDescription();

  /** @return a list of dependencies the package has. */
  Set<? extends DependencyDescription> getDependencies();

  /**
   * @return a list of classes and/or packages that should be automatically loaded. Annotation will
   *     be submitted to their respective services. If empty it should implicitly mean all classes
   *     in the package's jar file.
   */
  Set<String> getAutoloadClasses();

  /**
   * @return classes and/or packages that should be excluded from being loaded and processed
   *     automatically. If used in combination with autoload classes, those have precedence (this
   *     list can then be used to exclude classes that would otherwise match the given autoload
   *     list).
   */
  Set<String> getAutoloadExcludedClasses();

  /**
   * Checks if this package description is valid, e.g. if all necessary values are present.
   *
   * @return true, if valid.
   */
  boolean isValid();
}
