package net.flintmc.transform.javassist;

import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public enum CtClassFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String className) throws NotFoundException {
      Collection<CtClass> classes = CtClassFilters.collectSuperClassesRecursive(source);

      for (CtClass ctClass : classes) {
        if (className.equals(ctClass.getName())) {
          return true;
        }
      }

      return false;
    }
  };

  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass)
      throws NotFoundException {
    Collection<CtClass> classes = new HashSet<>();

    if (ctClass.getSuperclass() != null) {
      classes.add(ctClass.getSuperclass());
      classes.addAll(collectSuperClassesRecursive(ctClass.getSuperclass()));
    }

    classes.addAll(Arrays.asList(ctClass.getInterfaces()));

    for (CtClass value : ctClass.getInterfaces()) {
      classes.addAll(CtClassFilters.collectSuperClassesRecursive(value));
    }

    return Collections.unmodifiableCollection(classes);
  }

  public abstract boolean test(CtClass source, String className) throws NotFoundException;
}
