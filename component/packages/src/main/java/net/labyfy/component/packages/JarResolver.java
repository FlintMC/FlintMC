package net.labyfy.component.packages;

import java.util.Set;
import java.util.jar.JarFile;

/** Responsible for finding classes that are contained within a jar file. */
public interface JarResolver {

  /**
   * Tries to read class names that are contained in a given jar file.
   *
   * @param jarFile the jar file to read.
   * @return a set of class names that are contained in this jar.
   */
  Set<String> resolveClasses(JarFile jarFile);

  /**
   * Tries to read class names that are contained in a given jar file and filters them for names,
   * that match against at least one class or package name in a given set.
   *
   * @param jarFile the jar file to read.
   * @param matches a set of class or package names.
   * @return a set of class names.
   */
  Set<String> resolveMatchingClasses(JarFile jarFile, Set<String> matches);

  /**
   * Tries to read class names that are contained in a given jar file and filters them for names,
   * that match against at least one class or package name in a given set but do not match against
   * one from another given set. If the set to match against is empty, this means that every class
   * name matches, while if the set to exclude names is empty, none should be excluded.
   *
   * @param jarFile the jar file to read.
   * @param matches a set of class or package names.
   * @param excludes a set of class or package names.
   * @return a set of class names.
   */
  Set<String> resolveMatchingClasses(JarFile jarFile, Set<String> matches, Set<String> excludes);
}
