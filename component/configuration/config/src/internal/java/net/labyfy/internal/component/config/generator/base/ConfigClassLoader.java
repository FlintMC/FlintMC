package net.labyfy.internal.component.config.generator.base;

public class ConfigClassLoader extends ClassLoader {

  public ConfigClassLoader(ClassLoader parent) {
    super(parent);
  }

  public Class<?> defineClass(String name, byte[] bytes) {
    return super.defineClass(name, bytes, 0, bytes.length);
  }

}
