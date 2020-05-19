package net.labyfy.component.packages.impl;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.JarResolver;

import java.util.Collections;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@Singleton
@Implement(JarResolver.class)
public class LabyJarResolver implements JarResolver {

  @Override
  public Set<String> resolveClasses(JarFile jarFile) {
    return Collections.list(jarFile.entries()).stream()
        .map(ZipEntry::getName)
        .filter(name -> name.toLowerCase().endsWith(".class"))
        .map(name -> name.replace("/", ".").replace(".class", ""))
        .collect(Collectors.toSet());
  }

  @Override
  public Set<String> resolveMatchingClasses(JarFile jarFile, Set<String> matches) {
    return resolveClasses(jarFile).stream()
        .filter(name -> matches.isEmpty() || matches.stream().anyMatch(name::startsWith))
        .collect(Collectors.toSet());
  }

  @Override
  public Set<String> resolveMatchingClasses(
      JarFile jarFile, Set<String> matches, Set<String> excludes) {
    return resolveClasses(jarFile).stream()
        .filter(
            name -> {
              if (matches.isEmpty() || matches.stream().anyMatch(name::startsWith))
                return excludes == null || excludes.stream().noneMatch(name::startsWith);
              else return false;
            })
        .collect(Collectors.toSet());
  }
}
