package net.labyfy.component.transform.shadow;

import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(optionalProperties = {
    @Property(value = MethodProxy.class, allowMultiple = true),
    @Property(value = FieldGetter.class, allowMultiple = true),
    @Property(value = FieldSetter.class, allowMultiple = true)
})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transitive
@AutoLoad(round = 10)
public @interface Shadow {
  String value();
}
