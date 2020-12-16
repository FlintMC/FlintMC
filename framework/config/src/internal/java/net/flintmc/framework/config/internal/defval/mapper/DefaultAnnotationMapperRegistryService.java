package net.flintmc.framework.config.internal.defval.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(DefaultAnnotationMapperRegistry.class)
@Service(DefaultAnnotationMapper.class)
public class DefaultAnnotationMapperRegistryService
    implements ServiceHandler<DefaultAnnotationMapper>, DefaultAnnotationMapperRegistry {

  private final Map<Class<? extends Annotation>, DefaultAnnotationMapperHandler<?>> mappers;

  @Inject
  private DefaultAnnotationMapperRegistryService() {
    this.mappers = new HashMap<>();
  }

  @Override
  public Collection<Class<? extends Annotation>> getAnnotationTypes() {
    return Collections.unmodifiableCollection(this.mappers.keySet());
  }

  @Override
  public Object getDefaultValue(ConfigObjectReference reference, Annotation annotation) {
    DefaultAnnotationMapperHandler handler = this.mappers.get(annotation.annotationType());
    return handler != null ? handler.getDefaultValue(reference, annotation) : null;
  }

  @Override
  public void discover(AnnotationMeta<DefaultAnnotationMapper> meta) {
    this.mappers.put(
        meta.getAnnotation().value(),
        InjectionHolder.getInjectedInstance(
            CtResolver.get(meta.getClassIdentifier().getLocation())));
  }
}
