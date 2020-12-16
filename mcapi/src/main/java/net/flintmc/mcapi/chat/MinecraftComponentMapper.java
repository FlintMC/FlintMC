package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.ComponentMappingException;

/**
 * Mapper between the minecraft components and the flint components.
 */
public interface MinecraftComponentMapper {

  /**
   * Creates a new {@link ChatComponent} by using the given minecraft component as the base.
   *
   * @param handle The non-null minecraft component
   * @return The new Flint component or {@code null} if the given component was invalid
   * @throws ComponentMappingException If the given object is no minecraft component
   */
  ChatComponent fromMinecraft(Object handle) throws ComponentMappingException;

  /**
   * Creates a new minecraft component by using the given Flint component as the base.
   *
   * @param component The non-null Flint component
   * @return The new minecraft component or {@code null} if the given component was invalid
   */
  Object toMinecraft(ChatComponent component);
}
