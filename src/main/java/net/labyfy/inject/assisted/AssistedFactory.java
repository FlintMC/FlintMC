package net.labyfy.inject.assisted;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Identifier
@Transitive
public @interface AssistedFactory {
  Class<?> value();
}
