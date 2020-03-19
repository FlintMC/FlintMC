package net.labyfy.component.transform.javassist;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.*;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.mappings.ClassMappingProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassTransformContext {

  private final ClassMappingProvider labyClassMappingProvider;
  private final Collection<Predicate<CtClass>> filters;
  private final ClassTransform classTransform;
  private final Method method;
  private final Class owner;
  private CtClass ctClass;

  @AssistedInject
  private ClassTransformContext(
      ClassMappingProvider labyClassMappingProvider,
      @Assisted("filters") Collection<Predicate<CtClass>> filters,
      @Assisted ClassTransform classTransform,
      @Assisted Method method,
      @Assisted Class owner) {
    this.labyClassMappingProvider = labyClassMappingProvider;
    this.filters = Collections.unmodifiableCollection(filters);
    this.classTransform = classTransform;
    this.method = method;
    this.owner = owner;
  }

  @AssistedFactory(ClassTransformContext.class)
  public interface Factory {
    ClassTransformContext create(
        @Assisted("filters") Collection<Predicate<CtClass>> filters,
        ClassTransform classTransform,
        Method method,
        Class owner);
  }

  public CtField getField(String name) {
    try {
      return this.ctClass.getField(name);
    } catch (NotFoundException e) {
      return null;
    }
  }

  public CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers) {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    try {
      CtMethod ctMethod =
          CtMethod.make(
              String.format("%s %s %s {%s}", javaModifier, returnType, name, body), this.ctClass);
      this.ctClass.addMethod(ctMethod);
      return ctMethod;
    } catch (CannotCompileException e) {
      e.printStackTrace();
    }
    return null;
  }

  public CtMethod addMethod(String src) {
    try {
      CtMethod make = CtMethod.make(src, this.ctClass);
      this.ctClass.addMethod(make);
      return make;
    } catch (CannotCompileException e) {
      e.printStackTrace();
    }
    return null;
  }

  public CtMethod getOwnerMethod(String name, String desc) {
    try {
      return this.ctClass.getMethod(name, desc);
    } catch (NotFoundException e) {
      return null;
    }
  }

  public CtField addField(Class type, String name, Modifier... modifiers) {
    return this.addField(type.getName(), name, modifiers);
  }

  public CtField addField(String type, String name, Modifier... modifiers) {
    return this.addField(type, name, "null", modifiers);
  }

  public CtField addField(Class type, String name, String value, Modifier... modifiers) {
    return this.addField(type.getName(), name, value, modifiers);
  }

  public CtField addField(String type, String name, String value, Modifier... modifiers) {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    try {
      CtField make =
          CtField.make(
              String.format("%s %s %s = %s;", javaModifier, type, name, value), this.ctClass);
      this.ctClass.addField(make);
      return make;
    } catch (CannotCompileException e) {
      e.printStackTrace();
    }
    return null;
  }

  public CtMethod getDeclaredMethod(String name, Class... classes) {
    CtClass[] ctClasses = new CtClass[classes.length];
    for (int i = 0; i < classes.length; i++) {
      try {
        ctClasses[i] = this.ctClass.getClassPool().get(classes[i].getName());
      } catch (NotFoundException e) {
        e.printStackTrace();
      }
    }
    try {
      return this.getCtClass()
          .getDeclaredMethod(
              this.labyClassMappingProvider
                  .get(this.ctClass.getName())
                  .getMethod(name, classes)
                  .getName(),
              ctClasses);
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Collection<Predicate<CtClass>> getFilters() {
    return this.filters;
  }

  public Method getOwnerMethod() {
    return method;
  }

  public ClassTransform getClassTransform() {
    return classTransform;
  }

  public Class getOwner() {
    return owner;
  }

  public CtClass getCtClass() {
    return ctClass;
  }

  public void setCtClass(CtClass ctClass) {
    this.ctClass = ctClass;
  }
}
