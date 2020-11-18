package net.flintmc.framework.config.annotation;

import net.flintmc.framework.config.generator.ParsedConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to exclude a method from parsing and storing it in a config. If provided, the method will
 * just be ignored and won't appear in {@link ParsedConfig#getConfigReferences()}.
 *
 * @see Config
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigExclude {
}
