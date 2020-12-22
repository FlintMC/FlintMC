package net.flintmc.mcapi.settings.flint.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * Marks a method in a {@link Config} with an {@link ApplicableSetting} as native, that means that
 * it is defined by Minecraft itself and not by any FlintMC package. This annotation is only
 * intended to be used internally.
 *
 * @see Config
 * @see ApplicableSetting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NativeSetting {

}
