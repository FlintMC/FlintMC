package net.labyfy.component.packages.impl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageClassLoader;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Implement(PackageClassLoader.class)
public class LabyPackageClassLoader extends URLClassLoader implements PackageClassLoader {
  private final Package owner;

  @AssistedInject
  public LabyPackageClassLoader(@Assisted Package owner) throws MalformedURLException {
    super(new URL[] {owner.getFile().toURI().toURL()}, Launch.classLoader);
    this.owner = owner;
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }

  @Override
  public ClassLoader asClassLoader() {
    return this;
  }

  @Override
  public Package getOwner() {
    return owner;
  }
}
