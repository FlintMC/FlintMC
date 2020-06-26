package net.labyfy.component.inject.assisted;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

/**
 * Declares an <b>interface</b> as a factory base for a given class.
 *
 * Factories can by used to instantiate classes which required parameters which can not be
 * injected directly. For that methods with the name {@code create} and a signature matching
 * the constructor of the implementation classes can be created, which then will be used to
 * construct the underlying implementations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transitive
@Identifier
@AutoLoad(priority = ASSISTED_FACTORY_PRIORITY, round = ASSISTED_FACTORY_ROUND)
public @interface AssistedFactory {
  /**
   * The class this assisted factory instantiates. May be an interface, in which case
   * classes implementing the interface are used for instantiation. The return types
   * of the {@code create} methods have to have a return type compatible with this type.
   *
   * @return The class this factory is able to instantiate
   */
  Class<?> value();
}
