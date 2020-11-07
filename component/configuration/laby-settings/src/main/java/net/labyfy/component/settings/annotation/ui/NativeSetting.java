package net.labyfy.component.settings.annotation.ui;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method in a {@link Config} with an {@link ApplicableSetting} as native, that means that it is defined by
 * Minecraft itself and not by any FlintMC package. This annotation is only intended to be used internally.
 *
 * @see Config
 * @see ApplicableSetting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NativeSetting {
}
