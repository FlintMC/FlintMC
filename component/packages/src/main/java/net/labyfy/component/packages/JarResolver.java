package net.labyfy.component.packages;


import java.util.Set;
import java.util.jar.JarFile;

public interface JarResolver {

    Set<String> resolveClasses(JarFile jarFile);

    Set<String> resolveMatchingClasses(JarFile jarFile, Set<String> matches);

    Set<String> resolveMatchingClasses(JarFile jarFile, Set<String> matches, Set<String> excludes);

}
