package net.labyfy.component.transform.javassist;

import javassist.CtClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Deprecated
public enum CtClassFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String className) {
      return CtClassFilters.collectSuperClassesRecursive(source).stream()
          .anyMatch(ctClass -> ctClass.getName().equals(className));
    }
  };

  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass) {
    try {

      Collection<CtClass> classes = new HashSet<>();
      if (ctClass.getSuperclass() != null) {
        classes.add(ctClass.getSuperclass());
        classes.addAll(collectSuperClassesRecursive(ctClass.getSuperclass()));
      }
      classes.addAll(Arrays.asList(ctClass.getInterfaces()));
      Arrays.stream(ctClass.getInterfaces())
          .map(CtClassFilters::collectSuperClassesRecursive)
          .forEach(classes::addAll);

      return Collections.unmodifiableCollection(classes);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Collections.emptyList();
  }

  public abstract boolean test(CtClass source, String className);
}
