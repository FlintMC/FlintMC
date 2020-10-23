package net.labyfy.component.config.generator;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collection;

/**
 * A config that is currently being implemented by a {@link ConfigGenerator}.
 */
public interface GeneratingConfig {

  /**
   * Retrieves the name of the config, unique per {@link ConfigGenerator} and usually the name (with package) of the
   * base interface that is annotated with {@link Config}.
   *
   * @return The non-null name of the generating config
   */
  String getName();

  /**
   * Retrieves the interface that is annotated with {@link Config} and used to generate the implementation.
   *
   * @return The non-null base interface
   */
  CtClass getBaseClass();

  /**
   * Retrieves a mutable collection with all methods in this generating config.
   *
   * @return The mutable collection with all methods
   */
  Collection<ConfigMethod> getAllMethods();

  /**
   * Retrieves a collection with all implementations that have been generated for this config. It also contains
   * interfaces within this config that are necessary for getters/setters to be implemented.
   * <p>
   * The collection doesn't support add operations.
   *
   * @return The non-null collection with all implementations
   */
  Collection<CtClass> getGeneratedImplementations();

  /**
   * Retrieves an implementation for the given interface that has been generated for this config.
   *
   * @param baseName The name of the interface that has been implemented
   * @return The implementation class or {@code null} if there is no implementation for an interface with the given name
   * @see #bindGeneratedImplementation(String, CtClass)
   */
  CtClass getGeneratedImplementation(String baseName);

  /**
   * Binds an implementation for the name of a specific interface to be generated for this config.
   *
   * @param baseName       The non-null name of the implemented interface
   * @param implementation The non-null generated implementation to be bound to the given name
   */
  void bindGeneratedImplementation(String baseName, CtClass implementation);

  /**
   * Retrieves the class loader that will be used to define the generated implementations.
   *
   * @return The non-null class loader
   */
  ClassLoader getClassLoader();

  /**
   * Factory for the {@link GeneratingConfig}.
   */
  @AssistedFactory(GeneratingConfig.class)
  interface Factory {

    /**
     * Creates a new {@link GeneratingConfig} with the given class.
     *
     * @param baseClass The non-null interface of the config, should be annotated with {@link Config}
     * @return The new non-null generating config
     */
    GeneratingConfig create(@Assisted("baseClass") CtClass baseClass);

  }

}
