package net.flintmc.framework.inject.assisted.factory;

import com.google.inject.*;
import net.flintmc.framework.inject.assisted.binding.BindingCollector;

import java.lang.annotation.Annotation;

public class AssistedFactoryModuleBuilder {

  private final BindingCollector bindings = new BindingCollector();

  public <T> AssistedFactoryModuleBuilder implement(Class<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(Class<T> source, TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), target);
  }

  public <T> AssistedFactoryModuleBuilder implement(TypeLiteral<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(
          TypeLiteral<T> source, TypeLiteral<? extends T> target) {
    return implement(Key.get(source), target);
  }

  public <T> AssistedFactoryModuleBuilder implement(
          Class<T> source, Annotation annotation, Class<? extends T> target) {
    return implement(source, annotation, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(
          Class<T> source, Annotation annotation, TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), annotation, target);
  }

  public <T> AssistedFactoryModuleBuilder implement(
          TypeLiteral<T> source, Annotation annotation, Class<? extends T> target) {
    return implement(source, annotation, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(
          TypeLiteral<T> source, Annotation annotation, TypeLiteral<? extends T> target) {
    return implement(Key.get(source, annotation), target);
  }

  public <T> AssistedFactoryModuleBuilder implement(
          Class<T> source, Class<? extends Annotation> annotationType, Class<? extends T> target) {
    return implement(source, annotationType, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(
          Class<T> source,
          Class<? extends Annotation> annotationType,
          TypeLiteral<? extends T> target) {
    return implement(TypeLiteral.get(source), annotationType, target);
  }

  public <T> AssistedFactoryModuleBuilder implement(
          TypeLiteral<T> source,
          Class<? extends Annotation> annotationType,
          Class<? extends T> target) {
    return implement(source, annotationType, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(
          TypeLiteral<T> source,
          Class<? extends Annotation> annotationType,
          TypeLiteral<? extends T> target) {
    return implement(Key.get(source, annotationType), target);
  }

  public <T> AssistedFactoryModuleBuilder implement(Key<T> source, Class<? extends T> target) {
    return implement(source, TypeLiteral.get(target));
  }

  public <T> AssistedFactoryModuleBuilder implement(Key<T> source, TypeLiteral<? extends T> target) {
    bindings.addBinding(source, target);
    return this;
  }


  public <F> Module build(Class<F> factoryInterface) {
    return build(TypeLiteral.get(factoryInterface));
  }

  public <F> Module build(TypeLiteral<F> factoryInterface) {
    return build(Key.get(factoryInterface));
  }

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
