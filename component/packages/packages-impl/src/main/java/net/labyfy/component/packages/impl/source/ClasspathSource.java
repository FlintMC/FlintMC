package net.labyfy.component.packages.impl.source;

import net.labyfy.component.launcher.LaunchController;

import java.net.URL;

public class ClasspathSource implements PackageSource {
  @Override
  public URL findResource(String path) {
    return LaunchController.getInstance().getRootLoader().findResource(path);
  }
}
