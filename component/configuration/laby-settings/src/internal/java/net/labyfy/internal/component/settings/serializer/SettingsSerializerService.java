package net.labyfy.internal.component.settings.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.Identifier;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;
import net.labyfy.component.settings.serializer.SettingsSerializer;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

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
