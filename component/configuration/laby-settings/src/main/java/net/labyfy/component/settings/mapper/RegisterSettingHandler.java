package net.labyfy.component.settings.mapper;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

// annotated class needs to implement SettingHandler
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface RegisterSettingHandler {

  // e.g. ComponentSetting.class
  Class<? extends Annotation> value();

}
