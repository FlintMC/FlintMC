package net.labyfy.component.transform.hook;

import javassist.CtClass;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.base.structure.representation.Types;
import net.labyfy.component.transform.javassist.CtClassFilters;
import net.minecraft.launchwrapper.Launch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public enum HookFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String className) {
      return HookFilters.collectSuperClassesRecursive(source).stream()
          .anyMatch(clazz -> clazz.getName().equals(className));
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
          .map(HookFilters::collectSuperClassesRecursive)
          .forEach(classes::addAll);

      return Collections.unmodifiableCollection(classes);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Collections.emptyList();
  }

  public abstract boolean test(CtClass source, String className);
}
