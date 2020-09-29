package net.labyfy.component.configuration;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Identifier
@Transitive
public @interface Configuration {

  /**
   * Retrieves the path of the configuration.
   *
   * @return The path of the configuration.
   */
  String value();

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.FIELD})
  @interface Entry {

    /**
     * Retrieves the name of the entry.
     *
     * @return The entry name.
     */
    String value();

  }

}
