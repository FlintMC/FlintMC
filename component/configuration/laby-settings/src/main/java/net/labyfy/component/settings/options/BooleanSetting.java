package net.labyfy.component.settings.options;

import net.labyfy.component.config.defval.annotation.DefaultBoolean;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a boolean input, the stored type has to be a boolean.
 * <p>
 * The resulting json for the {@link net.labyfy.component.settings.serializer.JsonSettingsSerializer} will contain:
 * <ul>
 *   <li>'value' with the value from the setting as a boolean</li>
 * </ul>
 *
 * @see ApplicableSetting
 * @see DefaultBoolean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = boolean.class, name = "boolean")
public @interface BooleanSetting {

}
