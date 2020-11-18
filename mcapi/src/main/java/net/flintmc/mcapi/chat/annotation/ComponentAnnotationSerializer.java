package net.flintmc.mcapi.chat.annotation;

import net.flintmc.mcapi.chat.component.ChatComponent;

/** Serializer to map {@link Component} annotations to {@link ChatComponent}s. */
public interface ComponentAnnotationSerializer {

  /**
   * Maps the given component into a {@link ChatComponent}.
   *
   * @param component The non-null component annotation to be deserialized
   * @return The new non-null component
   * @see Component#value()
   */
  ChatComponent deserialize(Component component);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * @param components The non-null component annotation to be deserialized
   * @return The new non-null component
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * <p>If {@code components.length} is 0, {@code def} will be returned.
   *
   * @param components The non-null component annotation to be deserialized
   * @param def The non-null optional component to be used if {@code components.length} is 0
   * @return The new non-null component or {@code def}
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components, ChatComponent def);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * <p>If {@code components.length} is 0, {@code def} will be parsed into a {@link ChatComponent}
   * with {@link ComponentSerializer.Factory#legacy()} and returned.
   *
   * @param components The non-null component annotation to be deserialized
   * @param def The non-null optional string to be used if {@code components.length} is 0
   * @return The new non-null component or {@code def}
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components, String def);
}
