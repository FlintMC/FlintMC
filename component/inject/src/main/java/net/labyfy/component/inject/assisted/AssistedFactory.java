package net.labyfy.component.inject.assisted;

import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.labyfy.base.structure.AutoLoadPriorityConstants.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transitive
@Identifier
@AutoLoad(priority = ASSISTED_FACTORY_PRIORITY, round = ASSISTED_FACTORY_ROUND)
public @interface AssistedFactory {
  Class<?> value();
}
