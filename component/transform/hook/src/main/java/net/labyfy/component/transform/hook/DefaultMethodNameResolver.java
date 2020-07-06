package net.labyfy.component.transform.hook;

import net.labyfy.component.commons.resolve.AnnotationResolver;

import javax.inject.Singleton;

@Singleton
@Deprecated
public class DefaultMethodNameResolver implements AnnotationResolver<Hook, String> {

  public String resolve(Hook hook) {
    return hook.methodName();
  }
}
