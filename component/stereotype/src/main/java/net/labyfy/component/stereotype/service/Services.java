package net.labyfy.component.stereotype.service;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Services {

  Service[] value();

}
