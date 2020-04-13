package net.labyfy.component.packages;

import java.util.Optional;
import java.util.jar.JarFile;

public interface PackageDescriptionLoader {

  boolean isDescriptionPresent(JarFile file);

  Optional<PackageDescription> loadDescription(JarFile file);
}
