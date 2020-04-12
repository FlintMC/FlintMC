package net.labyfy.component.packages.impl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.PackageClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Implement(PackageClassLoader.class)
public class LabyPackageClassLoader extends URLClassLoader implements PackageClassLoader {

  @AssistedInject
  public LabyPackageClassLoader(
      @Named("delegationClassLoader") ClassLoader delegationLoader, @Assisted File jarFile)
      throws MalformedURLException {
    super(new URL[] {jarFile.toURI().toURL()}, delegationLoader);
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }
}
