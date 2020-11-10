package net.flintmc.transform.javassist.internal.factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.internal.DefaultClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
@Implement(ClassTransformContext.Factory.class)
public class DefaultClassTransformContextFactory implements ClassTransformContext.Factory {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private DefaultClassTransformContextFactory(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @Override
  public ClassTransformContext create(CtClass ctClass) {
    return new DefaultClassTransformContext(this.classMappingProvider, ctClass);
  }
}
