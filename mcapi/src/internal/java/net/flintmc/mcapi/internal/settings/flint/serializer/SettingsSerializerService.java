package net.flintmc.mcapi.internal.settings.flint.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.Identifier;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;

@Singleton
@Service(SettingsSerializer.class)
public class SettingsSerializerService implements ServiceHandler<SettingsSerializer> {

  private final JsonSettingsSerializer serializer;

  @Inject
  public SettingsSerializerService(JsonSettingsSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public void discover(AnnotationMeta<SettingsSerializer> meta) throws ServiceNotFoundException {
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass handlerType = identifier.getLocation();

    SettingsSerializationHandler handler = InjectionHolder.getInjectedInstance(CtResolver.get(handlerType));

    this.serializer.registerHandler(meta.getAnnotation().value(), handler);
  }
}
