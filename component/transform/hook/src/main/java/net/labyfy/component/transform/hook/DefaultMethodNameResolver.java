package net.labyfy.component.transform.hook;

import net.labyfy.base.structure.resolve.AnnotationResolver;

import javax.inject.Singleton;

@Singleton
public class DefaultMethodNameResolver implements AnnotationResolver<Hook, String> {

  public String resolve(Hook hook) {
    return hook.methodName();
  }
}