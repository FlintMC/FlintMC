package net.flintmc.mcapi.chat.internal.serializer;

import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.internal.serializer.gson.DefaultGsonComponentSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import org.apache.logging.log4j.Logger;

@Singleton
public class DefaultComponentSerializerFactory implements ComponentSerializer.Factory {

  private final ComponentSerializer legacy;
  private final ComponentSerializer plain;
  private final GsonComponentSerializer gson;

  public DefaultComponentSerializerFactory(Logger logger, ComponentBuilder.Factory componentFactory, boolean legacyHover) {
    this.legacy = new PlainComponentSerializer(logger, true); // plain serializer with all colors/formatting
    this.plain = new PlainComponentSerializer(logger, false); // plain serializer without any colors/formatting
    this.gson = new DefaultGsonComponentSerializer(logger, componentFactory, legacyHover); // in 1.16 the hoverEvent has completely changed
  }

  @Override
  public ComponentSerializer legacy() {
    return this.legacy;
  }

  @Override
  public ComponentSerializer plain() {
    return this.plain;
  }

  @Override
  public GsonComponentSerializer gson() {
    return this.gson;
  }
}
