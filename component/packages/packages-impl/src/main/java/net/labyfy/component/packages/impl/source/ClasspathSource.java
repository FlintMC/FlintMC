package net.labyfy.component.packages.impl.source;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ClasspathSource implements PackageSource {
  @Override
  public URL findResource(String path) {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }

  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }
}
