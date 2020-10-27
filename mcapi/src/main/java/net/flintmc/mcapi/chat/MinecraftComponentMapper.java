package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.ComponentMappingException;

/** Mapper between the minecraft components and the Labyfy components. */
public interface MinecraftComponentMapper {

  /**
   * Creates a new {@link ChatComponent} by using the given minecraft component as the base.
   *
   * @param handle The non-null minecraft component
   * @return The new Labyfy component or {@code null} if the given component was invalid
   * @throws ComponentMappingException If the given object is no minecraft component
   */
  ChatComponent fromMinecraft(Object handle) throws ComponentMappingException;

  /**
   * Creates a new minecraft component by using the given Labyfy component as the base.
   *
   * @param component The non-null Labyfy component
   * @return The new minecraft component or {@code null} if the given component was invalid
   */
  Object toMinecraft(ChatComponent component);
}
