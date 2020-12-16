package net.flintmc.mcapi.chat.component;

/**
 * A component that allows to display the numeric value of an entry in the scoreboard.
 */
public interface ScoreComponent extends ChatComponent {

  /**
   * Retrieves the name of this component or {@code null} if no name has been set. The client will
   * display the value out of the scoreboard where this key is the line that is displayed next to
   * the value.
   *
   * @return The name in the scoreboard or {@code null} if no name has been provided
   * @see #name(String)
   */
  String name();

  /**
   * Sets the name of this component. The client will display the value out of the scoreboard where
   * this key is the line that is displayed next to the value.
   *
   * @param name The new non-null name for this component
   */
  void name(String name);

  /**
   * Retrieves the name of the objective of this component or {@code null} if no objective has been
   * set. The client will use this to get the objective containing the value to display.
   *
   * @return The objective in the scoreboard or {@code null} if no objective has been provided
   * @see #objective(String)
   */
  String objective();

  /**
   * Sets the name of the objective of this component. The client will use this to get the objective
   * containing the value to display.
   *
   * @param objective The non-null name of the objective for this component
   */
  void objective(String objective);
}
