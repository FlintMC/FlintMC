/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.packages.internal;

import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageClassLoader;
import net.flintmc.framework.packages.internal.source.PackageSource;
import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.ChildClassLoader;
import net.flintmc.launcher.classloading.RootClassLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;

/** Default package loader implementation. */
public class DefaultPackageClassLoader extends ClassLoader
    implements PackageClassLoader, ChildClassLoader {
  static {
    registerAsParallelCapable();
  }

  private final Package owner;
  private final PackageSource source;

  /**
   * Constructs a new {@link DefaultPackageClassLoader} and registers it with the {@link
   * RootClassLoader} of the {@link LaunchController}.
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

  /** {@inheritDoc} */
  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return LaunchController.getInstance().getRootLoader().findClass(name);
  }

  /** {@inheritDoc} */
  @Override
  public ClassLoader asClassLoader() {
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Package getOwner() {
    return owner;
  }

  /** {@inheritDoc} */
  @Override
  public URL childFindResource(String name, boolean forClassLoad) {
    return findResource(name);
  }

  /** {@inheritDoc} */
  @Override
  public Enumeration<URL> commonFindResources(String name) throws IOException {
    return source.findResources(name);
  }

  /** {@inheritDoc} */
  @Override
  public Enumeration<URL> commonFindAllResources() throws IOException, URISyntaxException {
    return source.findAllResources();
  }

  /** {@inheritDoc} */
  @Override
  protected URL findResource(String name) {
    return source.findResource(name);
  }

  /** {@inheritDoc} */
  @Override
  public java.lang.Package commonDefinePackage(
      String name,
      String specTitle,
      String specVersion,
      String specVendor,
      String implTitle,
      String implVersion,
      String implVendor,
      URL sealBase) {
    return definePackage(
        name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
    return defineClass(name, b, off, len, null);
  }

  /** {@inheritDoc} */
  @Override
  public java.lang.Package commonGetPackage(String name) {
    return getPackage(name);
  }

  /** {@inheritDoc} */
  @Override
  public String getClassloaderName() {
    return "FlintPackageLoader[" + owner.getName() + "]";
  }
}
