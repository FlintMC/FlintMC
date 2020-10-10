package net.labyfy.component.processing.autoload;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.*;

public interface DetectableAnnotationProvider {
  default void register(List<AnnotationMeta> consumer) {
  }

  class AnnotationMeta<T extends Annotation> {

    private final ElementKind elementType;
    private final Identifier identifier;
    private final T annotation;
    private final Collection<AnnotationMeta<?>> metaData;

    public AnnotationMeta(ElementKind elementType, Identifier identifier, T annotation, AnnotationMeta<?>... metaData) {
      this.elementType = elementType;
      this.identifier = identifier;
      this.annotation = annotation;
      this.metaData = Arrays.asList(metaData);
    }

    public T getAnnotation() {
      return annotation;
    }

    public ElementKind getElementKind() {
      return elementType;
    }

    public <K extends Identifier<?>> K getIdentifier() {
      return (K) identifier;
    }

    public Collection<AnnotationMeta<?>> getMetaData() {
      return Collections.unmodifiableCollection(metaData);
    }

    public <K extends Annotation> Collection<AnnotationMeta<K>> getMetaData(Class<K> clazz) {
      List<AnnotationMeta<K>> annotationMetas = new ArrayList<>();
      for (AnnotationMeta<?> metaDatum : this.metaData) {
        if (metaDatum.getAnnotation().annotationType().equals(clazz)) {
          annotationMetas.add((AnnotationMeta<K>) metaDatum);
        }
      }
      return annotationMetas;
    }

    public interface Identifier<T> {
      T getLocation();
    }

    public static class ClassIdentifier implements Identifier<CtClass> {
      private final String name;

      public ClassIdentifier(String name) {
        this.name = name;
      }

      @Override
      public CtClass getLocation() {
        try {
          return ClassPool.getDefault().get(this.name);
        } catch (NotFoundException e) {
          throw new IllegalStateException(e);
        }
      }

      public String getName() {
        return name;
      }
    }

    public static class MethodIdentifier implements Identifier<CtMethod> {
      private final String owner;
      private final String name;
      private final String[] parameters;

      public MethodIdentifier(String owner, String name, String... parameters) {
        this.owner = owner;
        this.name = name;
        this.parameters = parameters;
      }

      public String getOwner() {
        return owner;
      }

      public String[] getParameters() {
        return parameters;
      }

      public String getName() {
        return name;
      }

      @Override
      public CtMethod getLocation() {
        try {
          return ClassPool.getDefault().get(this.owner).getDeclaredMethod(this.name, ClassPool.getDefault().get(this.parameters));
        } catch (NotFoundException e) {
          throw new IllegalStateException(e);
        }
      }
    }

  }

}
