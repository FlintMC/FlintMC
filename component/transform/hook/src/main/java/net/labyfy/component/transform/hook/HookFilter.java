package net.labyfy.component.transform.hook;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.type.Type;

import java.lang.annotation.*;

@Identifier(requireParent = true)
@Transitive
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HookFilter {
  HookFilters value();

  Type type();
}
