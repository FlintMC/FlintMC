package net.labyfy.component.config.modifier;

import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface ConfigModifierRegistry {

  Collection<ConfigModificationHandler> getHandlers();

  <A extends Annotation> A modify(ConfigObjectReference reference, A annotation);

}
