package net.labyfy.component.transform.hook;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.representation.Type;

import java.lang.annotation.*;

@Identifier(requireParent = true)
@Transitive
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HookFilter {
  HookFilters value();

  Type type();
}
