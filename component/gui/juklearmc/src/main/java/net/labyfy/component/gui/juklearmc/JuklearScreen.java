package net.labyfy.component.gui.juklearmc;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.component.gui.screen.ScreenName;

import java.lang.annotation.*;

/**
 * Marks a class as a juklear screen. The target class must implement
 * {@link net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transitive
@Identifier
public @interface JuklearScreen {
  /**
   * The origin type of the screen, used for constructing the {@link ScreenName}
   *
   * @return The type of the screen origin
   * @see ScreenName#getType()
   */
  ScreenName.Type type() default ScreenName.Type.FROM_MINECRAFT;

  /**
   * The identifier value of the screen, used for constructing the {@link ScreenName}
   *
   * @return The identifier value of the screen
   * @see ScreenName#getIdentifier()
   */
  String value();
}
