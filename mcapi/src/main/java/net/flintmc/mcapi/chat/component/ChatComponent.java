package net.flintmc.mcapi.chat.component;

import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;

/**
 * The basic component for all implementations of the chat component. Every message in the chat
 * consists of those components.
 */
public interface ChatComponent {

  /**
   * Retrieves an array of all enabled formats in this component. Any modification will have no
   * effect on the component.
   *
   * @return The non-null array of all enabled formats
   * @see #toggleFormat(ChatFormat, boolean)
   * @see #hasFormat(ChatFormat)
   */
  ChatFormat[] formats();

  /**
   * Retrieves whether this component has the given format.
   *
   * @param format The non-null format to check for
   * @return {@code true} if this component has the given format or {@code false} if not
   * @see #toggleFormat(ChatFormat, boolean)
   * @see #formats()
   */
  boolean hasFormat(ChatFormat format);

  /**
   * Enables/Disables the given format in this component. If the component is already in the given
   * state, this method will have no effect.
   *
   * @param format The format to enable/disable
   * @param enabled Whether the format should be enabled or not
   */
  void toggleFormat(ChatFormat format, boolean enabled);

  /**
   * Retrieves the color of this component or {@link ChatColor#WHITE} if no color has been set.
   *
   * @return The non-null color of this component
   * @see #color(ChatColor)
   */
  ChatColor color();

  /**
   * Sets the color of this component to the given color. If {@code null} has been provided, the
   * color will be changed to {@link ChatColor#WHITE}.
   *
   * @param color The new color of this component
   */
  void color(ChatColor color);

  /**
   * Retrieves the insertion of this component or {@code null} if no insertion has been specified.
   *
   * @return The insertion of this component or {@code null}
   * @see #insertion(String)
   */
  String insertion();

  /**
   * Sets the insertion of this component to the given value. If {@code null} has been provided, the
   * insertion will be removed. The insertion will be appended to the current chat input value when
   * the player shift-left-clicks on this component.
   *
   * @param insertion The new insertion of this component
   */
  void insertion(String insertion);

  /**
   * Retrieves the click event of this component which will be executed when the player clicks on
   * this component.
   *
   * @return The click event of this component or {@code null} if no click event has been provided.
   * @see #clickEvent(ClickEvent)
   */
  ClickEvent clickEvent();

  /**
   * Sets the click event of this component to the given value. If {@code null} has been provided,
   * nothing will happen when the player clicks on this component.
   *
   * @param event The new click event of this component or {@code null} to remove the click event
   */
  void clickEvent(ClickEvent event);

  /**
   * Retrieves the hover event of this component which will be displayed when the player hovers over
   * this component.
   *
   * @return The hover event of this component or {@code null} if no hover event has been provided.
   * @see #hoverEvent(HoverEvent)
   */
  HoverEvent hoverEvent();

  /**
   * Sets the hover event of this component to the given value. If {@code null} has been provided,
   * nothing will happen when the player hovers over this component.
   *
   * @param event The new hover event of this component or {@code null} to remove the hover event
   */
  void hoverEvent(HoverEvent event);

  /**
   * Gets an array of all extras of this component. Modification of this array won't have any effect
   * on this component.
   *
   * @return The non-null array of all extras in this component
   */
  ChatComponent[] extras();

  /**
   * Sets the extras of this component to the given array.
   *
   * @param extras The new non-null array of extra components
   */
  void extras(ChatComponent[] extras);

  /**
   * Adds the given component as an extra to this component.
   *
   * @param extra The new non-null extra for this component
   */
  void append(ChatComponent extra);

  /**
   * Gets the contents of this component like they will be displayed in the chat, but without
   * applying the specified {@link ChatFormat}s and {@link ChatColor}. This method ignores the
   * {@link #extras()} of this component.
   *
   * @return A non-null string with the contents of this component
   */
  String getUnformattedText();

  /**
   * Creates a copy of this component with the exact same contents as in this component, but
   * modifications on this component won't affect the new copy, nor do modifications to the copy.
   *
   * @return A new non-null component as a copy of this component
   */
  ChatComponent copy();

  /**
   * Checks whether this component has specified any values (format, color, click event, hover
   * event). In the implementations like {@link TextComponent} it also checks for fields like the
   * text.
   *
   * @return whether this component is empty or not
   */
  boolean isEmpty();
}
