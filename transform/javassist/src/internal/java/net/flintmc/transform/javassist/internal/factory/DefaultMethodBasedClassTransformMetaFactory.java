package net.flintmc.transform.javassist.internal.factory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.MethodBasedClassTransformMeta;
import net.flintmc.transform.javassist.internal.DefaultMethodBasedClassTransformMeta;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Implement(MethodBasedClassTransformMeta.Factory.class)
public class DefaultMethodBasedClassTransformMetaFactory implements MethodBasedClassTransformMeta.Factory {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final ClassMappingProvider classMappingProvider;
  private final Logger logger;
  private final Map<String, String> launchArguments;

  @Inject
  private DefaultMethodBasedClassTransformMetaFactory(
          ClassTransformContext.Factory classTransformContextFactory,
          ClassMappingProvider classMappingProvider,
          Logger logger,
          @Named("launchArguments") Map launchArguments) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.classMappingProvider = classMappingProvider;
    this.logger = logger;
    this.launchArguments = launchArguments;
  }

  @Override
  public MethodBasedClassTransformMeta create(AnnotationMeta annotationMeta) {
    return new DefaultMethodBasedClassTransformMeta(
            this.classTransformContextFactory,
            this.classMappingProvider,
            this.logger,
            annotationMeta,
            this.launchArguments
    );
  }
}
