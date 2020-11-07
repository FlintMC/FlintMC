package net.flintmc.framework.inject.assisted;

import com.google.inject.Inject;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When used in tandem with {@link AssistedFactoryModuleBuilder}, constructor annotated with {@link AssistedInject}
 * indicate that multiple constructors can be injected, each with different parameters. {@link AssistedInject}
 * annotations should not be mixed {@link Inject} annotations. The assisted parameters must exactly match one
 * corresponding factory method within the factory interface, but the parameters do not need to be in the same
 * order. Constructors annotated with {@link AssistedInject} <b>are</b> created by <b>Guice</b> and receive all the
 * benefits (such as AOP).
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface AssistedInject {

}
