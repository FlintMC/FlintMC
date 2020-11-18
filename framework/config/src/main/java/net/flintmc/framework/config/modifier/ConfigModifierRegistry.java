package net.flintmc.framework.config.modifier;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Registry containing all {@link ConfigModificationHandler}s for the modification of annotations inside a {@link
 * Config}.
 *
 * @see ConfigModificationHandler
 */
public interface ConfigModifierRegistry {

  /**
   * Retrieves a collection of all handlers that are registered in this registry.
   *
   * @return The non-null immutable collection of all handlers in this registry
   */
  Collection<ConfigModificationHandler> getHandlers();

  /**
   * Modifies the given annotation with the appropriate handlers in this registry.
   *
   * @param reference  The non-null reference where this annotation is located
   * @param annotation The non-null annotation to be modified
   * @param <A>        The type of the annotation to be modified
   * @return The non-null modified annotation, if there were no modifications, the {@code annotation} without any
   * modifications will be returned
   */
  <A extends Annotation> A modify(ConfigObjectReference reference, A annotation);

}
