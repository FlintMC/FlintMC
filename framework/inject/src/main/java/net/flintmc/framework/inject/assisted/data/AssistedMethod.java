package net.flintmc.framework.inject.assisted.data;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Represents a data interface about how a method in an assisted inject factory will be assisted.
 */
public interface AssistedMethod {

  /**
   * Retrieves teh factory method that is being assisted.
   *
   * @return The factory method that is being assisted.
   */
  Method getFactoryMethod();

  /**
   * Retrieves the implementation type that will be created when the method is used.
   *
   * @return The implementation type that will be created when the method is used.
   */
  TypeLiteral<?> getImplementationType();

  /**
   * Retrieves teh constructor that will be used to construct instances of the implementation.
   *
   * @return The constructor that will be used to construct instances of the implementation.
   */
  Constructor<?> getImplementationConstructor();

  /**
   * Retrieves all non-assisted dependencies required to construct and inject the implementation.
   *
   * @return All non-assisted dependencies required to construct and inject the implementation.
   */
  Set<Dependency<?>> getDependencies();

}
