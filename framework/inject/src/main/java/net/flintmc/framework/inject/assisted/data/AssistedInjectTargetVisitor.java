package net.flintmc.framework.inject.assisted.data;

import com.google.inject.spi.BindingTargetVisitor;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;

/**
 * Represents a visitor for the assisted factories.
 *
 * <p>If your {@link BindingTargetVisitor} implements this interface,
 * bindings created by using {@link AssistedFactoryModuleBuilder} will be visited through this
 * interface.</p>
 *
 * @param <T> The type of the binding.
 * @param <V> Any type to be returned by the visit method. Use {@link Void} with return {@code null}
 *            if no return type is needed.
 */
public interface AssistedInjectTargetVisitor<T, V> extends BindingTargetVisitor<T, V> {

  /**
   * Visits an {@link AssistedInjectBinding} created through {@link AssistedFactoryModuleBuilder}.
   *
   * @param assistedInjectBinding The assisted inject binding to be visited.
   * @return Any type to be returned by the visit method. Use {@link Void} with return {@code null}
   * if no return type is needed.
   */
  V visit(AssistedInjectBinding<? extends T> assistedInjectBinding);

}
