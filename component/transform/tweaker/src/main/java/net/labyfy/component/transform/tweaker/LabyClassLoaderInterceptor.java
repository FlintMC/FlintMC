package net.labyfy.component.transform.tweaker;

import net.minecraft.launchwrapper.LaunchClassLoader;

import java.net.URL;

public class LabyClassLoaderInterceptor {

  private final LaunchClassLoader launchClassLoader;

  private LabyClassLoaderInterceptor(LaunchClassLoader launchClassLoader) {
    this.launchClassLoader = launchClassLoader;
  }

  protected void addURL(URL url) {
    this.launchClassLoader.addURL(url);
  }

  public static LabyClassLoaderInterceptor create(LaunchClassLoader launchClassLoader) {
    return new LabyClassLoaderInterceptor(launchClassLoader);
  }

}
