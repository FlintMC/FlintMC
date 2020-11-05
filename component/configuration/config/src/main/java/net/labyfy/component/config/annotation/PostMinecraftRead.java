package net.labyfy.component.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// use with @Config so that it will be read after the POST_MINECRAFT_INITIALIZE task
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PostMinecraftRead {
}
