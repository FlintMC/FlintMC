package net.labyfy.component.packages;

import java.util.List;

public interface PackageDescription {

  String getName();

  String getDisplayName();

  String getVersion();

  List<String> getAuthors();

  String getDescription();

  List<? extends DependencyDescription> getDependencies();

  List<String> getAutoloadClasses();

  boolean isValid();

}
