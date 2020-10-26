package net.labyfy.component.transform.hook;

import net.flintmc.processing.autoload.DetectableAnnotation;
import net.labyfy.component.stereotype.type.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@DetectableAnnotation(requiresParent = true)
public @interface HookFilter {
  HookFilters value();

  Type type();
}
