package net.flintmc.framework.inject.assisted;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Declares an <b>interface</b> as a factory base for a given class.
 *
 * <p>Factories can be used to instantiate classes which required parameters which can not be
 * injected directly. For that methods with the name {@code create} and a signature matching the
 * constructor of the implementation classes can be created, which then will be used to construct
 * the underlying implementations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface AssistedFactory {

  /**
   * The class this assisted factory instantiates. May be an interface, in which case classes
   * implementing the interface are used for instantiation. The return types of the {@code create}
   * methods have to have a return type compatible with this type.
   *
   * @return The class this factory is able to instantiate
   */
  Class<?> value();
}
