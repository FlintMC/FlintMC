package net.labyfy.internal.component.config.defval.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;
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
    return this.mappers.keySet();
  }

  @Override
  public Object getDefaultValue(ConfigObjectReference reference, Annotation annotation) {
    DefaultAnnotationMapperHandler handler = this.mappers.get(annotation.annotationType());
    return handler != null ? handler.getDefaultValue(reference, annotation) : null;
  }

  @Override
  public void discover(AnnotationMeta<DefaultAnnotationMapper> meta) {
    this.mappers.put(meta.getAnnotation().value(), InjectionHolder.getInjectedInstance(CtResolver.get(meta.getClassIdentifier().getLocation())));
  }
}
