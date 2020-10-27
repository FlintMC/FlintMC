package net.flintmc.framework.stereotype.service;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Services {

  Service[] value();
}
