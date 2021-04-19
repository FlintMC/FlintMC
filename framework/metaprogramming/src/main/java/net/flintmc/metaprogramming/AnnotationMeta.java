/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.metaprogramming;

import net.flintmc.metaprogramming.identifier.*;
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
  private final Identifier<?> identifier;
  private final T annotation;
  private final String version;
  private final PackageMeta packageMeta;
  private final long cacheId;
  private final Collection<AnnotationMeta<?>> metaData;

  public AnnotationMeta(
      ElementKind elementType,
      Identifier<?> identifier,
      T annotation,
      String version,
      PackageMeta packageMeta,
      long cacheId,
      AnnotationMeta<?>... metaData) {
    this.elementType = elementType;
    this.identifier = identifier;
    this.annotation = annotation;
    this.version = version;
    this.packageMeta = packageMeta;
    this.cacheId = cacheId;
    this.metaData = Arrays.asList(metaData);
  }

  /**
   * @return the target annotation instance
   */
  public T getAnnotation() {
    return annotation;
  }

  /**
   * @return the element type which this annotation annotates
   */
  public ElementKind getElementKind() {
    return elementType;
  }

  /**
   * @return A generic Identifier which holds properties of the location where the annotation is
   * placed at
   */
  @SuppressWarnings("unchecked")
  public <K extends Identifier<?>> K getIdentifier() {
    return (K) identifier;
  }

  /**
   * @return A method Identifier which holds properties of the location where the annotation is
   * placed at
   */
  public MethodIdentifier getMethodIdentifier() {
    return (MethodIdentifier) this.identifier;
  }

  public FieldIdentifier getFieldIdentifier() {
    return (FieldIdentifier) this.identifier;
  }

  /**
   * @return A class Identifier which holds properties of the location where the annotation is
   * placed at
   */
  public ClassIdentifier getClassIdentifier() {
    return (ClassIdentifier) this.identifier;
  }

  /**
   * @return A constructor Identifier which holds properties of the location where the annotation is
   * placed at
   */
  public ConstructorIdentifier getConstructorIdentifier() {
    return (ConstructorIdentifier) this.identifier;
  }

  /**
   * @return all provided child metadata. Type defined in {@link DetectableAnnotation#metaData()}
   */
  public Collection<AnnotationMeta<?>> getMetaData() {
    return Collections.unmodifiableCollection(metaData);
  }

  /**
   * Determines whether this annotation meta is applicable to the currently running version.
   *
   * @param version The version to test for
   * @return {@code true} if the meta is applicable for the version, {@code false} otherwise
   */
  public boolean isApplicableForVersion(String version) {
    return this.version == null || this.version.equals(version);
  }

  /**
   * Retrieves meta data about the package owning this annotation meta.
   *
   * @return The package owning this annotation meta
   */
  public PackageMeta getOwningPackageMeta() {
    return packageMeta;
  }

  /**
   * @return an ID that changes when the implementation for the {@link DetectableAnnotationProvider}
   * is recompiled. Can be used as a key to invalidate cached for operations being performed by
   * ServiceHandlers.
   */
  public long getCacheId() {
    return this.cacheId;
  }

  /**
   * @param clazz the annotation class of the child metadata to look for
   * @param <K>   the annotation type of the child metadata to look for
   * @return all provided child metadata of the type clazz
   */
  @SuppressWarnings("unchecked")
  public <K extends Annotation> Collection<AnnotationMeta<K>> getMetaData(Class<K> clazz) {
    List<AnnotationMeta<K>> annotationMetas = new ArrayList<>();
    for (AnnotationMeta<?> metaDatum : this.metaData) {
      if (metaDatum.getAnnotation().annotationType().equals(clazz)) {
        annotationMetas.add((AnnotationMeta<K>) metaDatum);
      }
    }
    return annotationMetas;
  }
}
