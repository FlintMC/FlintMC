package net.labyfy.component.processing.autoload;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents the location of the associated {@link DetectableAnnotation}. Implementations are
 * {@link ClassIdentifier} and {@link MethodIdentifier}. Other target types are not yet supported,
 * but it is planned to extend the functionality here.
 *
 * @param <T> by default the javassist representation of the target type
 */
public class AnnotationMeta<T extends Annotation> {

  private final ElementKind elementType;
  private final Identifier identifier;
  private final T annotation;
  private final Collection<AnnotationMeta<?>> metaData;

  public AnnotationMeta(
      ElementKind elementType, Identifier identifier, T annotation, AnnotationMeta<?>... metaData) {
    this.elementType = elementType;
    this.identifier = identifier;
    this.annotation = annotation;
    this.metaData = Arrays.asList(metaData);
  }

  /** @return the target annotation instance */
  public T getAnnotation() {
    return annotation;
  }

  /** @return the element type which this annotation annotates */
  public ElementKind getElementKind() {
    return elementType;
  }

  /**
   * @return A generic Identifier which holds properties of the location where the annotation is
   *     placed at
   */
  public <K extends Identifier<?>> K getIdentifier() {
    return (K) identifier;
  }

  /**
   * @return A method Identifier which holds properties of the location where the annotation is
   *     placed at
   */
  public MethodIdentifier getMethodIdentifier() {
    return ((MethodIdentifier) this.identifier);
  }

  /**
   * @return A class Identifier which holds properties of the location where the annotation is
   *     placed at
   */
  public ClassIdentifier getClassIdentifier() {
    return ((ClassIdentifier) this.identifier);
  }

  /**
   * @return all provided child metadata. Type defined in {@link DetectableAnnotation#metaData()}
   */
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
        return ClassPool.getDefault()
            .get(this.owner)
            .getDeclaredMethod(this.name, ClassPool.getDefault().get(this.parameters));
      } catch (NotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
