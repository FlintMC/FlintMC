package net.labyfy.internal.component.packages;

import net.labyfy.internal.component.packages.source.PackageSource;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.launcher.classloading.ChildClassLoader;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageClassLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;

/**
 * Default package loader implementation.
 */
public class DefaultPackageClassLoader extends ClassLoader implements PackageClassLoader, ChildClassLoader {
  private final Package owner;
  private final PackageSource source;

  /**
   * Constructs a new {@link DefaultPackageClassLoader} and registers it with the
   * {@link net.labyfy.component.launcher.classloading.RootClassLoader} of the {@link LaunchController}.
   *
   * @param owner The package owning this class loader
   * @throws IOException If the jar file could not be read.
   */
  DefaultPackageClassLoader(Package owner) throws IOException {
    super(LaunchController.getInstance().getRootLoader());
    this.owner = owner;
    this.source = PackageSource.of(owner);
    LaunchController.getInstance().getRootLoader().registerChild(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return LaunchController.getInstance().getRootLoader().findClass(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassLoader asClassLoader() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Package getOwner() {
    return owner;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL childFindResource(String name, boolean forClassLoad) {
    return findResource(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> commonFindResources(String name) throws IOException {
    return source.findResources(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> commonFindAllResources() throws IOException, URISyntaxException {
    return source.findAllResources();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected URL findResource(String name) {
    return source.findResource(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public java.lang.Package commonDefinePackage(
      String name,
      String specTitle,
      String specVersion,
      String specVendor,
      String implTitle,
      String implVersion,
      String implVendor,
      URL sealBase
  ) {
    return definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
    return defineClass(name, b, off, len, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public java.lang.Package commonGetPackage(String name) {
    return getPackage(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassloaderName() {
    return "LabyPackageLoader[" + owner.getName() + "]";
  }

  static {
    registerAsParallelCapable();
  }
}
