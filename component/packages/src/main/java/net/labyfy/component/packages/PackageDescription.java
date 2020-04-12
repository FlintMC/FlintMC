package net.labyfy.component.packages;

import java.util.List;

public interface PackageDescription {

  String getName();

  String getVersions();

  List<String> getAuthors();

  String getDescription();

  List<DependencyDescription> getDependencies();

  List<String> getTaskHolders();
}
