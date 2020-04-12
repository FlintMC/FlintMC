package net.labyfy.component.packages;

import java.io.File;

public interface PackageDescriptionLoader {

  boolean isDescriptionPresent(File file);

  PackageDescription loadDescription(File file);
}
