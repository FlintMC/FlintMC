package net.labyfy.component.launcher.classloading;

public class ClassloaderExtension extends ClassLoader {
  public ClassloaderExtension(ClassLoader parent) {
    super(parent);
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }

  static {
    ClassLoader.registerAsParallelCapable();
  }
}
