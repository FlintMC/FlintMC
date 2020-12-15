package net.flintmc.framework.inject.assisted.data;

import com.google.inject.Key;
import java.util.Collection;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;

/**
 * Represents a binding for a factory created by {@link AssistedFactoryModuleBuilder}.
 *
 * @param <T> The fully qualified type of the factory.
 */
public interface AssistedInjectBinding<T> {

  /**
   * Retrieves the {@link Key} for the factory binding.
   *
   * @return The {@link Key} for the factory binding.
   */
  Key<T> getKey();

  /**
   * Retrieves an {@link AssistedMethod} for each method in the factory.
   *
   * @return An {@link AssistedMethod} for each method in the factory.
   */
  Collection<AssistedMethod> getAssistedMethods();
}
