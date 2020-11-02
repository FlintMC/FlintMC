package net.labyfy.component.config.modifier;

import java.lang.annotation.Annotation;

public interface ConfigModificationHandler {

  Annotation modify(Annotation annotation);

}
