package net.labyfy.component.gui.juklearmc;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transitive
@Identifier
public @interface StandaloneJuklearComponent {


}
