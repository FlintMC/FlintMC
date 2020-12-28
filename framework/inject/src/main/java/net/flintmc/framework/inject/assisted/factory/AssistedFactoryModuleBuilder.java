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

package net.flintmc.framework.inject.assisted.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import java.lang.annotation.Annotation;
import net.flintmc.framework.inject.assisted.binding.BindingCollector;

/**
 * Provides the factory that combines the caller's arguments with values supplied by the injector to
 * construct objects.
 */
public class AssistedFactoryModuleBuilder {

  private final BindingCollector bindings = new BindingCollector();

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(Class<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Class<T> source, TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), target);
  }

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source, TypeLiteral<? extends T> target) {
    return implement(Key.get(source), target);
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotation The annotation of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Class<T> source, Annotation annotation, Class<? extends T> target) {
    return implement(source, annotation, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotation The annotation of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Class<T> source, Annotation annotation, TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), annotation, target);
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotation The annotation of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source, Annotation annotation, Class<? extends T> target) {
    return implement(source, annotation, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotation The annotation of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source, Annotation annotation, TypeLiteral<? extends T> target) {
    return implement(Key.get(source, annotation), target);
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotationType The annotation type of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Class<T> source, Class<? extends Annotation> annotationType, Class<? extends T> target) {
    return implement(source, annotationType, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotationType The annotation type of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Class<T> source,
      Class<? extends Annotation> annotationType,
      TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), annotationType, target);
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotationType The annotation type of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source,
      Class<? extends Annotation> annotationType,
      Class<? extends T> target) {
    return implement(source, annotationType, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source} and the annotation type of the {@code
   * source}.
   *
   * @param source The source of the binding.
   * @param annotationType The annotation type of the source.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      TypeLiteral<T> source,
      Class<? extends Annotation> annotationType,
      TypeLiteral<? extends T> target) {
    return implement(Key.get(source, annotationType), target);
  }

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(Key<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  /**
   * Bind the given {@code target} to the {@code source}.
   *
   * @param source The source of the binding.
   * @param target The target of the source binding.
   * @param <T> The type of the bindings.
   * @return This builder, for chaining.
   */
  public <T> AssistedFactoryModuleBuilder implement(
      Key<T> source, TypeLiteral<? extends T> target) {
    bindings.addBinding(source, target);
    return this;
  }

  /**
   * Built a module with the given factory interface.
   *
   * @param factoryInterface The interface for the factory.
   * @param <F> The type of the factory.
   * @return The built module.
   */
  public <F> Module build(Class<F> factoryInterface) {
    return build(TypeLiteral.get(factoryInterface));
  }

  /**
   * Built a module with the given factory interface.
   *
   * @param factoryInterface The interface for the factory.
   * @param <F> The type of the factory.
   * @return The built module
   */
  public <F> Module build(TypeLiteral<F> factoryInterface) {
    return build(Key.get(factoryInterface));
  }

  /**
   * Built a module with the given factory interface.
   *
   * @param factoryInterface The interface for the factory.
   * @param <F> The type of the factory.
   * @return The built module
   */
  public <F> Module build(final Key<F> factoryInterface) {
    return new AbstractModule() {
      @Override
      protected void configure() {
        Provider<F> provider = new AssistedFactoryProvider<>(factoryInterface, bindings);
        bind(factoryInterface).toProvider(provider);
      }
    };
  }
}
