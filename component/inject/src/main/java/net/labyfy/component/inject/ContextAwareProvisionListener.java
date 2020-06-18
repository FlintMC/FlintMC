package net.labyfy.component.inject;

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
import java.util.function.Function;

public class ContextAwareProvisionListener<T> implements ProvisionListener, Provider<T> {

  private final Key<T> key;
  private final BiFunction<InjectionPoint, Key<T>, T> provider;
  private final ThreadLocal<T> current = new ThreadLocal<>();

  private ContextAwareProvisionListener(BiFunction<InjectionPoint, Key<T>, T> provider, Key<T> key) {
    this.provider = provider;
    this.key = key;
  }

  public <T2> void onProvision(ProvisionListener.ProvisionInvocation<T2> pi) {
    if (!pi.getBinding().getKey().equals(key))
      throw new RuntimeException("Unexpected key -- got " + pi.getBinding().getKey() + ", expected " + key);
    try {
      List<DependencyAndSource> chain = pi.getDependencyChain();
      if (chain.isEmpty())
        throw new RuntimeException("This should never be empty");
      DependencyAndSource das = chain.get(chain.size() - 1);
      InjectionPoint ip = das == null || das.getDependency() == null ? null : das.getDependency().getInjectionPoint();
      T value = provider.apply(ip, this.key);
      if (value == null)
        throw new RuntimeException("Context aware providers should never return null");
      current.set(value);
      pi.provision();
    } finally {
      current.remove();
    }
  }

  @Override
  public T get() {
    T value = current.get();
    if (value == null)
      throw new RuntimeException("There is no current value -- this should never happen");
    return value;
  }

  public static <T> void bindContextAwareProvider(Binder binder, Class<T> type, BiFunction<InjectionPoint, Key<T>, T> provider) {
    bindContextAwareProvider(binder, Key.get(type), provider);
  }

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