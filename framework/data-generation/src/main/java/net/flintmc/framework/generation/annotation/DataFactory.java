package net.flintmc.framework.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Declares an <b>interface</b> as a factory for a given data interface.
 *
 * <p>Using the annotation automatically generates an implementation of the given data interface
 * based on its {@link DataFieldMethod}s (getters and/or setters) and on the {@link
 * DataFactoryMethod} (factory create method). All {@link DataFieldMethod}s and {@link
 * DataFactoryMethod} parameters have to be annotated with {@link TargetDataField} to be able to
 * create the {@link DataField}s and method implementations correctly. An implementation of the
 * factory is created automatically too and is bound to the factory interface via Guice.
 *
 * <p>Example data class:
 *
 * <pre>
 * public interface SomeDataHolder {
 *
 *   &#64;TargetDataField("someData")
 *   String getSomeData();
 *
 *   &#64;TargetDataField("someData")
 *   void setSomeData(String someData);
 *
 *   &#64;DataFactory(SomeDataHolder.class)
 *   interface Factory {
 *
 *     SomeDataHolder create(&#64;TargetDataField("someData") String someData);
 *
 *     default SomeDataHolder create() {
 *       return this.create("defaultSomeData");
 *     }
 *   }
 * }
 * </pre>
 *
 * The actual names of the methods do not matter, everything is evaluated based on the {@link
 * TargetDataField} annotations and the method parameter- and return types. Already implemented
 * methods in the data- or factory interface are left untouched.
 *
 * <p>Example data creation:
 *
 * <pre>
 * public class SomeDataCreator {
 *
 *   private final SomeDataHolder.Factory someDataHolderFactory;
 *
 *   &#64;Inject
 *   public SomeDataCreator(SomeDataHolder.Factory someDataHolderFactory) {
 *     this.someDataHolderFactory = someDataHolderFactory;
 *   }
 *
 *   public void createSomeData() {
 *     SomeDataHolder someDataHolder =
 *       this.someDataHolderFactory.create("someData");
 *
 *     someDataHolder.setSomeData("anotherSomeData");
 *     System.out.println(someDataHolder.getSomeData());
 *   }
 * }
 * </pre>
 *
 * @see TargetDataField
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface DataFactory {

  /**
   * Defines the data interface which the factory will create an instance of.
   *
   * @return A data interface with getters and/or setters annotated with {@link TargetDataField}
   */
  Class<?> value();
}
