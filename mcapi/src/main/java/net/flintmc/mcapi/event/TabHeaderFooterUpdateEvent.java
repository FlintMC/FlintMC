package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.player.overlay.TabOverlay;

import javax.annotation.Nullable;

/**
 * This event will be fired when the tab header/footer is updated or reset (e.g. via {@link
 * TabOverlay#updateHeader(ChatComponent)} or {@link TabOverlay#updateFooter(ChatComponent)}). It
 * will be fired on both {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases, but
 * cancellation will only have an effect in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface TabHeaderFooterUpdateEvent extends Event, Cancellable {

  /**
   * Retrieves the new header/footer that has been set to be displayed.
   *
   * @return The new header/footer component or {@code null} if it has been reset
   */
  ChatComponent getNewValue();

  /**
   * Retrieves the type of the component that has been updated.
   *
   * @return The non-null type of the component that has been updated
   * @see Type
   */
  Type getType();

  /**
   * An enumeration of types of components that can be updated in the {@link
   * TabHeaderFooterUpdateEvent}.
   */
  enum Type {
    /** The header in the tab list has been updated. */
    HEADER,
    /** The footer in the tab list has been updated. */
    FOOTER
  }

  /** Factory for the {@link TabHeaderFooterUpdateEvent}. */
  @AssistedFactory(TabHeaderFooterUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link TabHeaderFooterUpdateEvent}.
     *
     * @param newValue The component that has been updated or {@code null} if it has been reset
     * @param type The non-null type of the component that has been updated
     * @return The new non-null {@link TabHeaderFooterUpdateEvent}.
     */
    TabHeaderFooterUpdateEvent create(
        @Assisted("new") @Nullable ChatComponent newValue, @Assisted Type type);
  }
}
