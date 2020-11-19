package net.flintmc.util.property;

public interface PropertyContextAware<
    T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>> {
  PropertyContext<T_PropertyContextAware> getPropertyContext();
}
