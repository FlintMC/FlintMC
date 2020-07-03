package net.labyfy.component.transform.javassist;

import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Deprecated
public enum CtClassFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String className) throws NotFoundException {
      return CtClassFilters.collectSuperClassesRecursive(source).stream()
          .anyMatch(ctClass -> ctClass.getName().equals(className));
    }
  };

  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass) throws NotFoundException {
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
