package net.flintmc.framework.inject.assisted.binding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Message;

import java.util.Collections;
import java.util.Map;

public class BindingCollector {

  private final Map<Key<?>, TypeLiteral<?>> bindings = Maps.newHashMap();

  public BindingCollector addBinding(Key<?> key, TypeLiteral<?> target) {
    if (bindings.containsKey(key)) {
      throw new ConfigurationException(
              ImmutableSet.of(new Message("Only one implementation can be specified for " + key)));
    }

    bindings.put(key, target);

    return this;
  }

  public Map<Key<?>, TypeLiteral<?>> getBindings() {
    return Collections.unmodifiableMap(bindings);
  }

  @Override
  public int hashCode() {
    return bindings.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof BindingCollector) && bindings.equals(((BindingCollector) obj).bindings);
  }

}
