package net.labyfy.component.transform.hook;

import com.google.inject.Singleton;
import net.labyfy.component.commons.resolve.AnnotationResolver;

@Singleton
@Deprecated
public class DefaultMethodNameResolver implements AnnotationResolver<Hook, String> {

  public String resolve(Hook hook) {
    return hook.methodName();
  }
}
