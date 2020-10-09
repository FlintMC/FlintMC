package net.labyfy.component.stereotype.identifier;

import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashSet;

public class IdentifierParser {

  public static Collection<IdentifierMeta<?>> collectIdentifiers(CtClass ctClass) throws ClassNotFoundException {
    Collection<IdentifierMeta<?>> identifierMetas = new HashSet<>();

    for (Object object : ctClass.getAnnotations()) {
      Annotation annotation = (Annotation) object;
      if (!annotation.annotationType().isAnnotationPresent(Transitive.class)) continue;
      if (!annotation.annotationType().isAnnotationPresent(Identifier.class)) continue;

      identifierMetas.add(new IdentifierMeta<>(annotation.annotationType().getAnnotation(Identifier.class), ElementType.TYPE, annotation, ctClass));
    }

    for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
      identifierMetas.addAll(collectIdentifiers(declaredMethod));
    }

    for (IdentifierMeta<?> identifierMeta : identifierMetas) {
      addChildren(identifierMeta);
    }

    return identifierMetas;
  }

  public static Collection<IdentifierMeta<?>> collectIdentifiers(CtMethod ctMethod) throws ClassNotFoundException {
    Collection<IdentifierMeta<?>> identifierMetas = new HashSet<>();

    for (Object object : ctMethod.getAnnotations()) {
      Annotation annotation = (Annotation) object;
      if (!annotation.annotationType().isAnnotationPresent(Transitive.class)) continue;
      if (!annotation.annotationType().isAnnotationPresent(Identifier.class)) continue;

      identifierMetas.add(new IdentifierMeta<>(annotation.annotationType().getAnnotation(Identifier.class), ElementType.METHOD, annotation, ctMethod));
    }

    return identifierMetas;
  }

  private static void addChildren(IdentifierMeta<?> identifierMeta) throws ClassNotFoundException {
    Collection<IdentifierMeta<?>> childIdentifierMeta = new HashSet<>();

    for (PropertyMeta propertyMeta : identifierMeta.getRequiredPropertyMeta()) {
      if (!propertyMeta.getAnnotationType().isAnnotationPresent(Identifier.class))
        throw new IllegalArgumentException("Target identifier child is not marked with @Identifier");

      if (identifierMeta.getTargetType() == ElementType.TYPE) {
        CtClass ctClass = identifierMeta.getTarget();
        if (!ctClass.hasAnnotation(propertyMeta.getAnnotationType()))
          throw new IllegalArgumentException("Identifier is missing child.");

        Identifier childIdentifier = propertyMeta.getAnnotationType().getDeclaredAnnotation(Identifier.class);
        IdentifierMeta<Annotation> childMeta = new IdentifierMeta<>(childIdentifier, ElementType.TYPE, (Annotation) ctClass.getAnnotation(propertyMeta.getAnnotationType()), ctClass);
        childIdentifierMeta.add(childMeta);
        addChildren(childMeta);
        //Todo add children
      } else if (identifierMeta.getTargetType() == ElementType.METHOD) {
        //Todo add children
        CtMethod ctMethod = identifierMeta.getTarget();
        if (!ctMethod.hasAnnotation(propertyMeta.getAnnotationType()))
          throw new IllegalArgumentException("Identifier is missing child.");

        Identifier childIdentifier = propertyMeta.getAnnotationType().getDeclaredAnnotation(Identifier.class);
        IdentifierMeta<Annotation> childMeta = new IdentifierMeta<>(childIdentifier, ElementType.METHOD, (Annotation) ctMethod.getAnnotation(propertyMeta.getAnnotationType()), ctMethod);
        childIdentifierMeta.add(childMeta);
        addChildren(childMeta);
      }
    }


    for (PropertyMeta propertyMeta : identifierMeta.getOptionalPropertyMeta()) {
      if (!propertyMeta.getAnnotationType().isAnnotationPresent(Identifier.class))
        throw new IllegalArgumentException("Target identifier child is not marked with @Identifier");

      if (identifierMeta.getTargetType() == ElementType.TYPE) {
        CtClass ctClass = identifierMeta.getTarget();
        if (!ctClass.hasAnnotation(propertyMeta.getAnnotationType()))
          continue;

        Identifier childIdentifier = propertyMeta.getAnnotationType().getDeclaredAnnotation(Identifier.class);
        IdentifierMeta<Annotation> childMeta = new IdentifierMeta<>(childIdentifier, ElementType.TYPE, (Annotation) ctClass.getAnnotation(propertyMeta.getAnnotationType()), ctClass);
        childIdentifierMeta.add(childMeta);
        addChildren(childMeta);
        //Todo add children
      } else if (identifierMeta.getTargetType() == ElementType.METHOD) {
        //Todo add children
        CtMethod ctMethod = identifierMeta.getTarget();
        if (!ctMethod.hasAnnotation(propertyMeta.getAnnotationType()))
          continue;

        Identifier childIdentifier = propertyMeta.getAnnotationType().getDeclaredAnnotation(Identifier.class);
        IdentifierMeta<Annotation> childMeta = new IdentifierMeta<>(childIdentifier, ElementType.METHOD, (Annotation) ctMethod.getAnnotation(propertyMeta.getAnnotationType()), ctMethod);
        childIdentifierMeta.add(childMeta);
        addChildren(childMeta);
      }
    }

    for (IdentifierMeta<?> meta : childIdentifierMeta) {
      identifierMeta.getProperties().put(meta.getAnnotation().annotationType(), meta);
    }
  }

}
