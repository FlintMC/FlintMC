package net.flintmc.framework.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;

/**
 * Assigns {@link DataFieldMethod}s in a data interface and the parameters of the {@link
 * DataFactoryMethod}s to a certain {@link DataField} in a data class. This is needed to properly
 * create an implementation of the data interface and the factory interface.
 *
 * @see DataFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface TargetDataField {

  /**
   * Defines the name of the {@link DataField} which this method or parameter targets.
   *
   * @return The name of the targeted data field
   */
  String value();
}
