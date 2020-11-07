package net.labyfy.component.settings.annotation.ui;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// class needs to be declared in the class where the "for" part is declared, if not, set declaring()
// sub settings in sub settings don't work

/**
 * Defines {@link ApplicableSetting}s in a {@link Config} to be sub settings of another {@link ApplicableSetting}.
 * <p>
 * The class where the with {@link SubSettingsFor} annotated interface is located has to be either an inner class of the
 * class where the parent setting is defined or {@link #declaring()} has to return the class where the parent setting is
 * defined.
 * <p>
 * Example of a parent setting with sub settings:
 *
 * <pre>
 *
 * <literal>@</literal>BooleanSetting
 * boolean getSomeParent();
 *
 * SubSettings getSubSettings();
 *
 * <literal>@</literal>SubSettingsFor("SomeParent")
 * interface SubSettings {
 *
 *   <literal>@</literal>StringSetting
 *   String getSomeSubSetting();
 *
 * }
 *
 * </pre>
 * <p>
 * This would mark everything in the SubSettings interface as a sub setting of getParent.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubSettingsFor {

  /**
   * Retrieves the name of the parent. If the getter is 'getX', the name would be 'X'
   *
   * @return The name of the parent
   * @see Config
   */
  String value();

  /**
   * Retrieves the declaring class of the parent. If this is not provided, the declaring class of the class where this
   * annotation is located will be used.
   *
   * @return The optional declaring class
   */
  Class<?> declaring() default Dummy.class;

  /**
   * Dummy for {@link #declaring()} to mark that no class is defined.
   */
  final class Dummy {
    private Dummy() {
    }
  }

}
