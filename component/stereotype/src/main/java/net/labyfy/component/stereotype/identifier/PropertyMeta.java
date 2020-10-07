package net.labyfy.component.stereotype.identifier;

import java.lang.annotation.Annotation;

public interface PropertyMeta {

  Class<? extends Annotation> getAnnotationType();

  boolean allowMultiple();

  boolean isRequired();
}
