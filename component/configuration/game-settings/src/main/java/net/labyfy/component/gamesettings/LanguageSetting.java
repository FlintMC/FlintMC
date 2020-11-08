package net.labyfy.component.gamesettings;

import net.labyfy.component.gamesettings.configuration.AccessibilityConfiguration;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a setting to show all available languages, the stored type has to be a
 * string.
 * <p>
 * The resulting json for the {@link JsonSettingsSerializer} will contain:
 * <ul>
 *   <li>'selected' with the value of the setting</li>
 *   <li>'languages' with a list of the names of all languages that are available</li>
 * </ul>
 *
 * @see ApplicableSetting
 * @see AccessibilityConfiguration#getLanguage()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "language")
public @interface LanguageSetting {

}
