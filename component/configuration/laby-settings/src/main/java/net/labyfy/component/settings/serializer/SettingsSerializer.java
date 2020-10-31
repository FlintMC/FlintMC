package net.labyfy.component.settings.serializer;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface SettingsSerializer {

  Class<? extends Annotation> value();

}
