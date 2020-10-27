package net.flintmc.framework.inject.util;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProvisionListener;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Utility class for creating context await injections.
 *
 * @param <T> The type that should be injected
 */
public class ContextAwareProvisionListener<T> implements ProvisionListener, Provider<T> {
  private final Key<T> key;
  private final BiFunction<InjectionPoint, Key<T>, T> provider;
  private final ThreadLocal<T> current = new ThreadLocal<>();

  /**
   * Constructs a new {@link ContextAwareProvisionListener} with the given provider and key.
   *
   * @param provider The provider to use for creating instances
   * @param key      The key to listen to
   */
  private ContextAwareProvisionListener(BiFunction<InjectionPoint, Key<T>, T> provider, Key<T> key) {
    this.provider = provider;
    this.key = key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T2> void onProvision(ProvisionListener.ProvisionInvocation<T2> pi) {
    if (!pi.getBinding().getKey().equals(key)) {
      // Only accept the our own key
      throw new IllegalArgumentException("Unexpected key -- got " + pi.getBinding().getKey() + ", expected " + key);
    }

    try {
      List<DependencyAndSource> chain = pi.getDependencyChain();
      if (chain.isEmpty()) {
        throw new UnsupportedOperationException("Empty dependency chain detected");
      }

      // Extract the real injection point
      DependencyAndSource das = chain.get(chain.size() - 1);
      InjectionPoint ip = das == null || das.getDependency() == null ? null : das.getDependency().getInjectionPoint();

      // Let the provider map the injection
      T value = provider.apply(ip, this.key);
      if (value == null) {
        throw new IllegalStateException("Context aware providers should never return null");
      }

      // Apply the injection
      current.set(value);
      pi.provision();
    } finally {
      current.remove();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T get() {
    T value = current.get();
    if (value == null) {
      // Should never happen
      throw new UnsupportedOperationException("Empty current value detected");
    }

    return value;
  }

  /**
   * Binds a new context aware provider to the given type.
   *
   * @param binder The binder to use for binding
   * @param type The type that should be used as a key
   * @param provider The provider used for creating instances
   * @param <T> The type to inject
   */
  public static <T> void bindContextAwareProvider(Binder binder, Class<T> type, BiFunction<InjectionPoint, Key<T>, T> provider) {
    bindContextAwareProvider(binder, Key.get(type), provider);
  }

  /**
   * Binds a new context aware provider to the given key.
   *
   * @param binder The binder to use for binding
   * @param key The key that should be used for detecting injections
   * @param provider The provider used for creating instances
   * @param <T> The type to inject
   */
  public static <T> void bindContextAwareProvider(Binder binder, Key<T> key, BiFunction<InjectionPoint, Key<T>, T> provider) {
    Matcher<Binding<?>> matcher = new AbstractMatcher<Binding<?>>() {
      @Override
      public boolean matches(Binding<?> binding) {
        return binding.getKey().equals(key);
      }
    };
    ContextAwareProvisionListener<T> impl = new ContextAwareProvisionListener<>(provider, key);
    binder.bind(key).toProvider(impl);
    binder.bindListener(matcher, impl);
  }
}