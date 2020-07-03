package net.labyfy.internal.component.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.*;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Deprecated
@Implement(ClassTransformContext.class)
public class InternalClassTransformContext implements ClassTransformContext {

  private final ClassMappingProvider labyClassMappingProvider;
  private final Collection<Predicate<CtClass>> filters;
  private final NameResolver nameResolver;
  private final ClassTransform classTransform;
  private final Method method;
  private final Class ownerClass;
  private final Object owner;
  private CtClass ctClass;

  @AssistedInject
  private InternalClassTransformContext(
      ClassMappingProvider labyClassMappingProvider,
      @Assisted("filters") Collection<Predicate<CtClass>> filters,
      @Assisted NameResolver nameResolver,
      @Assisted ClassTransform classTransform,
      @Assisted Method method,
      @Assisted Class ownerClass,
      @Assisted("owner") Object owner) {
    this.labyClassMappingProvider = labyClassMappingProvider;
    this.filters = Collections.unmodifiableCollection(filters);
    this.nameResolver = nameResolver;
    this.classTransform = classTransform;
    this.method = method;
    this.ownerClass = ownerClass;
    this.owner = owner;
  }

  public Class getOwnerClass() {
    return ownerClass;
  }

  public Optional<CtField> getField(String name) {
    try {
      return Optional.of(this.ctClass.getField(name));
    } catch (NotFoundException ignore) {
      return Optional.empty();
    }
  }

  public CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers) throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtMethod ctMethod =
        CtMethod.make(
            String.format("%s %s %s {%s}", javaModifier, returnType, name, body), this.ctClass);
    this.ctClass.addMethod(ctMethod);
    return ctMethod;
  }

  public CtMethod addMethod(String src) throws CannotCompileException {
    CtMethod make = CtMethod.make(src, this.ctClass);
    this.ctClass.addMethod(make);
    return make;
  }

  public Optional<CtMethod> getOwnerMethod(String name, String desc) {
    try {
      return Optional.of(this.ctClass.getMethod(name, desc));
    } catch (NotFoundException ignore) {
      return Optional.empty();
    }
  }

  public CtField addField(Class type, String name, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type.getName(), name, modifiers);
  }

  public CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type, name, "null", modifiers);
  }

  public CtField addField(Class type, String name, String value, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type.getName(), name, value, modifiers);
  }

  public CtField addField(String type, String name, String value, Modifier... modifiers) throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtField make =
        CtField.make(
            String.format("%s %s %s = %s;", javaModifier, type, name, value), this.ctClass);
    this.ctClass.addField(make);
    return make;
  }

  public Optional<CtMethod> getDeclaredMethod(String name, Class... classes) {
    try {
      CtClass[] ctClasses = new CtClass[classes.length];

      for (int i = 0; i < classes.length; i++) {
        ctClasses[i] = this.ctClass.getClassPool().get(classes[i].getName());
      }

      return Optional.of(this.getCtClass()
          .getDeclaredMethod(
              this.labyClassMappingProvider
                  .get(this.ctClass.getName())
                  .getMethod(name, classes)
                  .getName(),
              ctClasses));
    } catch (NotFoundException ignore) {
      return Optional.empty();
    }
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

  public Object getOwner() {
    return owner;
  }

  public NameResolver getNameResolver() {
    return nameResolver;
  }

  public CtClass getCtClass() {
    return ctClass;
  }

  public void setCtClass(CtClass ctClass) {
    this.ctClass = ctClass;
  }

}
