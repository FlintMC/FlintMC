package net.labyfy.component.settings.options.text;

import net.labyfy.chat.component.TextComponent;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// useful for color support in the chat like in LabyMod 3 the chat symbols
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = TextComponent.class, name = "component")
public @interface ComponentSetting {

}
