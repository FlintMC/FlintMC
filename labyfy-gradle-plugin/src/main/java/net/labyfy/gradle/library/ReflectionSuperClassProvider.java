package net.labyfy.gradle.library; // Created by leo on 25.09.19

import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.classloading.common.ClassInformation;
import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;
import net.labyfy.gradle.util.ASMUtils;
import org.objectweb.asm.tree.ClassNode;

import javax.inject.Singleton;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ReflectionSuperClassProvider implements SuperClassProvider {

  private RootClassLoader classLoader;

  protected ReflectionSuperClassProvider(File jarFile, Collection<File> libraries)
      throws NoSuchMethodException, MalformedURLException, InvocationTargetException,
      IllegalAccessException {
    this.classLoader = new RootClassLoader(new URL[]{});
    this.classLoader.addURLs(Collections.singleton(jarFile.toURI().toURL()));
    libraries.stream()
        .map(File::toPath)
        .filter(path -> (path.toFile().isFile() && path.toFile().getName().endsWith(".jar")))
        .forEach(
            path -> {
              File file = path.toFile();
              try {
                this.classLoader.addURLs(Collections.singleton(file.toURI().toURL()));
              } catch (MalformedURLException e) {
                e.printStackTrace();
              }
            });
  }

  public List<String> getSuperClass(String clazz) {
    try {
      ClassNode theClazz =
          ASMUtils.getNode(
              CommonClassLoaderHelper.retrieveClass(this.classLoader, clazz).getClassBytes());

      ClassInformation superClassInformation =
          CommonClassLoaderHelper.retrieveClass(this.classLoader, theClazz.superName);

      ClassNode superClass =
          superClassInformation == null
              ? null
              : ASMUtils.getNode(superClassInformation.getClassBytes());

      ArrayList<String> classes = new ArrayList<>();
      if (superClass != null) classes.add(superClass.name);
      if (theClazz.interfaces != null) {
        classes.addAll(theClazz.interfaces);
      }

      ArrayList<String> transitiveSuperClasses = new ArrayList<>();
      classes.forEach(c -> transitiveSuperClasses.addAll(getSuperClass(c)));
      classes.addAll(transitiveSuperClasses);
      return classes;
    } catch (Exception e) {
      // Not found, can be ignored
      return new ArrayList<>();
    }
  }
}
