package net.labyfy.component.transform.hook;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.base.structure.resolve.AnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(requireParent = true)
@Transitive
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HookFilter {
  HookFilters value();

  Type type();
}
