package net.labyfy.component.initializer.inject.module;

import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.io.IOException;

@Singleton
public class BindConstantModule extends AbstractModule {

  protected void configure() {
    this.bindNamedFilePath("modsFolder", "./Labyfy/mods");
    try {
      this.bindNamed(
          "obfuscated",
          (ClassPath.from(Launch.classLoader)
                  .getTopLevelClassesRecursive("net.minecraft.world")
                  .size()
              == 0));
      this.bindNamedFilePath("labyfyRoot", "./Labyfy");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void bindNamedFilePath(String name, String path) {
    this.bindNamed(name, path);
    this.bindNamed(name, new File(path));
  }

  private void bindNamed(String name, Object object) {
    this.bind(Key.get(((Class) object.getClass()), Names.named(name))).toInstance(object);
  }
}
