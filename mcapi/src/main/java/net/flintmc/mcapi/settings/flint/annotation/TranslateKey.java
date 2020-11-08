package net.flintmc.mcapi.settings.flint.annotation;

import net.flintmc.framework.config.annotation.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * This annotation can be used on an {@link ApplicableSetting} in a {@link Config} with the return type {@link Map} so
 * that the key of the map will be translated for the displayName. If it is not provided, the raw key of the map will be
 * used as a displayName.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TranslateKey {

}
