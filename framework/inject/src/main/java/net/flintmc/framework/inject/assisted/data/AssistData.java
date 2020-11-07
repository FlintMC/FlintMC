package net.flintmc.framework.inject.assisted.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import net.flintmc.framework.inject.assisted.thread.ThreadLocalProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class AssistData implements AssistedMethod {

  private final Constructor<?> constructor;
  private final Key<?> returnType;
  private final ImmutableList<Key<?>> paramTypes;
  private final TypeLiteral<?> implementationType;
  private final Set<Dependency<?>> dependencies;
  private final Method factoryMethod;
  private final boolean optimized;
  private final List<ThreadLocalProvider> providers;
  private Binding<?> cachedBinding;

  public AssistData(
          Constructor<?> constructor,
          Key<?> returnType,
          ImmutableList<Key<?>> paramTypes,
          TypeLiteral<?> implementationType,
          Method factoryMethod,
          Set<Dependency<?>> dependencies,
          boolean optimized,
          List<ThreadLocalProvider> providers) {
    this.constructor = constructor;
    this.returnType = returnType;
    this.paramTypes = paramTypes;
    this.implementationType = implementationType;
    this.factoryMethod = factoryMethod;
    this.dependencies = dependencies;
    this.optimized = optimized;
    this.providers = providers;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass())
            .add("ctor", constructor)
            .add("return type", returnType)
            .add("param type", paramTypes)
            .add("implementation type", implementationType)
            .add("dependencies", dependencies)
            .add("factory method", factoryMethod)
            .add("optimized", optimized)
            .add("providers", providers)
            .add("cached binding", cachedBinding)
            .toString();
  }

  @Override
  public Set<Dependency<?>> getDependencies() {
    return dependencies;
  }

  @Override
  public Method getFactoryMethod() {
    return factoryMethod;
  }

  @Override
  public Constructor<?> getImplementationConstructor() {
    return constructor;
  }

  @Override
  public TypeLiteral<?> getImplementationType() {
    return implementationType;
  }

  public Constructor<?> getConstructor() {
    return constructor;
  }

  public Key<?> getReturnType() {
    return returnType;
  }

  public ImmutableList<Key<?>> getParamTypes() {
    return paramTypes;
  }

  public boolean isOptimized() {
    return optimized;
  }

  public List<ThreadLocalProvider> getProviders() {
    return providers;
  }

  public Binding<?> getCachedBinding() {
    return cachedBinding;
  }

  public void setCachedBinding(Binding<?> cachedBinding) {
    this.cachedBinding = cachedBinding;
  }
}
