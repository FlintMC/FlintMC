package net.flintmc.mcapi.chat.builder;

import net.flintmc.mcapi.chat.component.ScoreComponent;

/** Builder for {@link ScoreComponent}s. */
public interface ScoreComponentBuilder extends ComponentBuilder<ScoreComponentBuilder> {

  /**
   * Sets the name of the current component. The client will display the value out of the scoreboard
   * where this key is the line that is displayed next to the value.
   *
   * @param name The non-null key for the scoreboard value
   * @return this
   */
  ScoreComponentBuilder name(String name);

  /**
   * Retrieves the name of the current component.
   *
   * @return The name of the current component in the scoreboard or {@code null} if no name has been
   *     provided
   * @see #name(String)
   */
  String name();

  /**
   * Sets the objective of the current component. The client will use this to get the objective
   * containing the value to display.
   *
   * @param objective The non-null name of the objective
   * @return this
   */
  ScoreComponentBuilder objective(String objective);

  /**
   * Retrieves the objective of the current component.
   *
   * @return The objective of the current component in the scoreboard or {@code null} if no
   *     objective has been provided
   * @see #objective(String)
   */
  String objective();
}
