package net.flintmc.util.property;

/**
 * Represents an owner of a {@link PropertyContext}.
 *
 * @param <T_PropertyContextAware> self reference. Just used for generic locking
 * @see PropertyContext
 */
public interface PropertyContextAware<
    T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>> {
  /**
   * @return the instance of the associated {@link PropertyContext}
   */
  PropertyContext<T_PropertyContextAware> getPropertyContext();
}
