package net.flintmc.framework.config.annotation;

import net.flintmc.framework.tasks.Tasks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used along with {@link Config} so that the config will be read from the storages after the
 * {@link Tasks#POST_MINECRAFT_INITIALIZE} task.
 * <p>
 * If this annotation is not provided, the config will directly be read after it has been discovered.
 *
 * @see Config
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PostMinecraftRead {
}
