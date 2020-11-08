package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.internal.chat.serializer.DefaultComponentSerializerFactory;
import org.apache.logging.log4j.Logger;

@Implement(value = ComponentSerializer.Factory.class, version = "1.15.2")
@Singleton
public class VersionedComponentSerializerFactory extends DefaultComponentSerializerFactory {

  @Inject
  public VersionedComponentSerializerFactory(
      Logger logger, ComponentBuilder.Factory componentFactory) {
    super(
        logger, componentFactory, true); // the legacy hover is enabled for every version below 1.16
  }
}
