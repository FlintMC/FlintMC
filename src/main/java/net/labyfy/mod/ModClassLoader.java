package net.labyfy.mod;

import com.google.common.base.Preconditions;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ModClassLoader extends URLClassLoader {

  private ModClassLoader(URL jarFile) {
    super(new URL[] {jarFile});
  }

  protected static ModClassLoader create(URL jarFile) {
    Preconditions.checkNotNull(jarFile);
    return new ModClassLoader(jarFile);
  }
}
