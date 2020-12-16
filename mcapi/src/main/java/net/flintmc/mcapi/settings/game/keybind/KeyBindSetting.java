package net.flintmc.mcapi.settings.game.keybind;

import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.render.gui.input.Key;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = Key.class, name = "keybind")
public @interface KeyBindSetting {}
