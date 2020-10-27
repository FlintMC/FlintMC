package net.flintmc.transform.minecraft;

import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.DetectableAnnotation;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link LateInjectedTransformer} to be registered
 * and detected automatically by a {@link ServiceHandler}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface MinecraftTransformer {

  /**
   * Defines the priority order in which the {@link LateInjectedTransformer}
   * will handle class transformations.
   * <p>
   * Lowest priority will be called first.
   *
   * @return the priority for class transformations
   */
  int priority() default 0;
}
