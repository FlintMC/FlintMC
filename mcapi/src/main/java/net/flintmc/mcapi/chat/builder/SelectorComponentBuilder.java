package net.flintmc.mcapi.chat.builder;

import java.util.Collection;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.component.SelectorComponent;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;

/**
 * Builder for {@link SelectorComponent}s.
 */
public interface SelectorComponentBuilder extends ComponentBuilder<SelectorComponentBuilder> {

  /**
   * Sets the type of the targeting entities to the current component.
   *
   * @param selector The non-null type of the entities that can be targeted
   * @return this
   */
  SelectorComponentBuilder selector(EntitySelector selector);

  /**
   * Retrieves the type of the targeting entities. Defaults to {@link EntitySelector#SELF}.
   *
   * @return The selector of the current component or {@code null} if no selector has been provided
   * @see #selector(EntitySelector)
   */
  EntitySelector selector();

  /**
   * Sets an option for the current component. If the value is {@code null}, the option will be
   * removed.
   *
   * @param option The non-null key of the option
   * @param value  The nullable value of the option
   * @return this
   */
  SelectorComponentBuilder option(String option, String value);

  /**
   * Retrieves an option out of the current component or {@code null}, if the given option has not
   * been set.
   *
   * @return The non-null values of the current component for the given option, empty if no option
   * with the given key is set.
   */
  Collection<String> option(String option);

  /**
   * Parses the selector and all options out of the given string. This method ignores any space in
   * the specified string. <br>
   * <br>
   *
   * <p>The allowed format looks like this: selectorShortcut[option1=value1,option2=value2] <br>
   * <br>
   *
   * <p>The option and value are split at the first `=` which means that the options may not
   * contain
   * an `=`, but it doesn't cause any issues in the values. The available selectorShortcuts can be
   * found in the {@link EntitySelector} class. <br>
   * <br>
   *
   * <p>As an example the input could like like this: e[limit=1,r=10]
   *
   * @param rawSelector The text in the specified format
   * @return this
   * @throws InvalidSelectorException The {@code rawSelector} doesn't have the correct format.
   */
  SelectorComponentBuilder parse(String rawSelector);
}
