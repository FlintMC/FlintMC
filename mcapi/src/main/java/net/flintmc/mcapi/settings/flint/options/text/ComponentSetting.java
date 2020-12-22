package net.flintmc.mcapi.settings.flint.options.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.chat.annotation.DefaultComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;

/**
 * The {@link ApplicableSetting} to define a string input with all pre defined {@link ChatColor
 * colors} and {@link ChatFormat formats}, the stored type has to be a {@link TextComponent}.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting, serialized with the {@link
 *       GsonComponentSerializer}
 * </ul>
 *
 * @see ApplicableSetting
 * @see DefaultComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = TextComponent.class, name = "component")
public @interface ComponentSetting {

}
