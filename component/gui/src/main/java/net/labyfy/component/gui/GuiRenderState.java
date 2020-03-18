package net.labyfy.component.gui;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Transitive
@Identifier(requireParent = true)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiRenderState {

  Type value();

  int priority() default 0;

  enum Type {
    INIT,
    RENDER
  }
}
