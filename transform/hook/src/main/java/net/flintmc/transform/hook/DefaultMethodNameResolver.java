package net.flintmc.transform.hook;

import com.google.inject.Singleton;
import net.flintmc.util.commons.resolve.AnnotationResolver;

@Singleton
public class DefaultMethodNameResolver implements AnnotationResolver<Hook, String> {

  public String resolve(Hook hook) {
    return hook.methodName();
  }
}
