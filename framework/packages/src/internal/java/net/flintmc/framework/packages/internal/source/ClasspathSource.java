package net.flintmc.framework.packages.internal.source;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Represents the classpath as a source.
 */
public class ClasspathSource implements PackageSource {
  /**
   * {@inheritDoc}
   */
  @Override
  public URL findResource(String path) {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> findAllResources() throws IOException {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }
}
