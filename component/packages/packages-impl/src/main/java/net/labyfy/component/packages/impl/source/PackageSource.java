package net.labyfy.component.packages.impl.source;

import net.labyfy.component.packages.Package;

import java.io.IOException;
import java.net.URL;

public interface PackageSource extends AutoCloseable {
  @Override
  default void close() throws IOException {}

  URL findResource(String path);

  static PackageSource of(Package pkg) {
    if(pkg.getFile() == null) {
      return new ClasspathSource();
    } else {
      return new FileSource(pkg.getFile());
    }
  }
}
