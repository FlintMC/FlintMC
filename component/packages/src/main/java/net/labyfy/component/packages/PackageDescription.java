package net.labyfy.component.packages;

import java.util.Set;

public interface PackageDescription {

  String getName();

  String getDisplayName();

  String getVersion();

  Set<String> getAuthors();

  String getDescription();

  Set<? extends DependencyDescription> getDependencies();

  Set<String> getAutoloadClasses();

  Set<String> getAutoloadExcludedClasses();

  boolean isValid();
}
