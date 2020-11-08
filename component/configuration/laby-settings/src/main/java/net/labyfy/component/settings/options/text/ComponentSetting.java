package net.labyfy.component.settings.options.text;

import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.chat.format.ChatFormat;
import net.labyfy.chat.serializer.GsonComponentSerializer;
import net.labyfy.component.config.defval.annotation.DefaultComponent;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a string input with all pre defined {@link ChatColor colors} and {@link
 * ChatFormat formats}, the stored type has to be a {@link TextComponent}.
 * <p>
 * The resulting json for the {@link JsonSettingsSerializer} will contain:
 * <ul>
 *   <li>'value' with the value from the setting, serialized with the {@link GsonComponentSerializer}</li>
 * </ul>
 *
 * @see DefaultComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = TextComponent.class, name = "component")
public @interface ComponentSetting {

}
