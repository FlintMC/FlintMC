package net.flintmc.mcapi.settings.flint.annotation.ui;

import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used with any {@link ApplicableSetting} to add an icon to the setting. If it is not set,
 * there will be no icon.
 *
 * <p>The resulting json from the {@link SettingsSerializationHandler} will contain an 'icon' string
 * with {@link #value()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Icon {

  // TODO implement the icons

  // empty = none
  String value();
}
