package net.labyfy.component.packages;

import java.util.List;

public interface DependencyDescription {

  String getName();

  List<String> getVersions();
}
