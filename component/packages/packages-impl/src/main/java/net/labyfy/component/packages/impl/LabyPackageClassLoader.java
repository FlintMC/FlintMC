package net.labyfy.component.packages.impl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.launcher.classloading.ChildClassLoader;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageClassLoader;
import net.labyfy.component.packages.impl.source.PackageSource;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;

@Implement(PackageClassLoader.class)
public class LabyPackageClassLoader extends ClassLoader implements PackageClassLoader, ChildClassLoader {
  private final Package owner;
  private final PackageSource source;

  @AssistedInject
  public LabyPackageClassLoader(@Assisted Package owner) {
    super(LaunchController.getInstance().getRootLoader());
    this.owner = owner;
    this.source = PackageSource.of(owner);
    LaunchController.getInstance().getRootLoader().registerChild(this);
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return LaunchController.getInstance().getRootLoader().findClass(name);
  }

  @Override
  public ClassLoader asClassLoader() {
    return this;
  }

  @Override
  public Package getOwner() {
    return owner;
  }

  @Override
  public URL childFindResource(String name, boolean forClassLoad) {
    return findResource(name);
  }

  @Override
  public Enumeration<URL> commonFindResources(String name) throws IOException {
    return source.findResources(name);
  }

  @Override
  public Enumeration<URL> commonFindAllResources() throws IOException {
    return source.findAllResources();
  }

  @Override
  protected URL findResource(String name) {
    return source.findResource(name);
  }

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

  @Override
  public Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
    return defineClass(name, b, off, len, null);
  }

  @Override
  public java.lang.Package commonGetPackage(String name) {
    return getPackage(name);
  }

  @Override
  public String getClassloaderName() {
    return "LabyPackageLoader[" + owner.getName() + "]";
  }

  static {
    registerAsParallelCapable();
  }
}
