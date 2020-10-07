package net.labyfy.internal.component.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.*;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Deprecated
@Implement(ClassTransformContext.class)
public class InternalClassTransformContext implements ClassTransformContext {

  private final ClassMappingProvider labyClassMappingProvider;
  private final Collection<Predicate<CtClass>> filters;
  private final NameResolver nameResolver;
  private final ClassTransform classTransform;
  private final CtMethod method;
  private final CtClass ownerClass;
  private CtClass ctClass;

  @AssistedInject
  private InternalClassTransformContext(
      ClassMappingProvider labyClassMappingProvider,
      @Assisted("filters") Collection<Predicate<CtClass>> filters,
      @Assisted NameResolver nameResolver,
      @Assisted ClassTransform classTransform,
      @Assisted CtMethod method,
      @Assisted CtClass ownerClass) {
    this.labyClassMappingProvider = labyClassMappingProvider;
    this.filters = Collections.unmodifiableCollection(filters);
    this.nameResolver = nameResolver;
    this.classTransform = classTransform;
    this.method = method;
    this.ownerClass = ownerClass;
  }

  @Override
  public CtClass getOwnerClass() {
    return ownerClass;
  }

  @Override
  public CtField getField(String name) throws NotFoundException {
    return this.ctClass.getField(name);
  }

  @Override
  public CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers) throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtMethod ctMethod =
        CtMethod.make(
            String.format("%s %s %s {%s}", javaModifier, returnType, name, body), this.ctClass);
    this.ctClass.addMethod(ctMethod);
    return ctMethod;
  }

  @Override
  public CtMethod addMethod(String src) throws CannotCompileException {
    CtMethod make = CtMethod.make(src, this.ctClass);
    this.ctClass.addMethod(make);
    return make;
  }

  @Override
  public CtMethod getOwnerMethod(String name, String desc) throws NotFoundException {
    return this.ctClass.getMethod(name, desc);
  }

  @Override
  public CtField addField(Class<?> type, String name, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type.getName(), name, modifiers);
  }

  @Override
  public CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type, name, "null", modifiers);
  }

  @Override
  public CtField addField(Class<?> type, String name, String value, Modifier... modifiers) throws CannotCompileException {
    return this.addField(type.getName(), name, value, modifiers);
  }

  @Override
  public CtField addField(String type, String name, String value, Modifier... modifiers) throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtField make =
        CtField.make(
            String.format("%s %s %s = %s;", javaModifier, type, name, value), this.ctClass);
    this.ctClass.addField(make);
    return make;
  }

  @Override
  public CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException {
    CtClass[] ctClasses = new CtClass[classes.length];

    for (int i = 0; i < classes.length; i++) {
      ctClasses[i] = this.ctClass.getClassPool().get(classes[i].getName());
    }

    return this.getCtClass()
        .getDeclaredMethod(
            this.labyClassMappingProvider
                .get(this.ctClass.getName())
                .getMethod(name, classes)
                .getName(),
            ctClasses);
  }

  @Override
  public Collection<Predicate<CtClass>> getFilters() {
    return this.filters;
  }

  @Override
  public CtMethod getOwnerMethod() {
    return method;
  }

  @Override
  public ClassTransform getClassTransform() {
    return classTransform;
  }

  @Override
  public NameResolver getNameResolver() {
    return nameResolver;
  }

  @Override
  public CtClass getCtClass() {
    return ctClass;
  }

  @Override
  public void setCtClass(CtClass ctClass) {
    this.ctClass = ctClass;
  }

}
