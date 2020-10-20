package net.labyfy.component.processing.autoload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RepeatingDetectableAnnotation {

  Class<? extends Annotation> value() default Annotation.class;

}
