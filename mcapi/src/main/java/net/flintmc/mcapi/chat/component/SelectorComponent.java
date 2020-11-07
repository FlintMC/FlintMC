package net.flintmc.mcapi.chat.component;

import com.google.common.collect.Multimap;
import net.flintmc.mcapi.chat.EntitySelector;

import java.util.Collection;

/** A component for the selection of entities. */
public interface SelectorComponent extends ChatComponent {

  /**
   * Retrieves the target selector for the type of target entities of this component or {@code null}
   * if no selector has been set.
   *
   * @return The selector or {@code null} if no selector has been provided
   * @see #selector(EntitySelector)
   */
  EntitySelector selector();

  /**
   * Sets the target selector for the type of target entities of this component.
   *
   * @param selector The new non-null selector for this component
   */
  void selector(EntitySelector selector);

  /**
   * Retrieves all options of this selector component.
   *
   * @return A non-null modifiable map containing all options and their values
   * @see #selectorOptions(Multimap)
   */
  Multimap<String, String> selectorOptions();

  /**
   * Sets the options of this component.
   *
   * @param options The new non-null options
   */
  void selectorOptions(Multimap<String, String> options);

  /**
   * Adds a specific option to this component. If the specified value is null, the option will be
   * removed out of this component.
   *
   * @param option The non-null key for the option
   * @param value The nullable value for the option
   */
  void selectorOption(String option, String value);

  /**
   * Retrieves the options for the given key out of this component.
   *
   * @param option The non-null key for the option
   * @return The non-null values for the given option, empty if no option with the given key is set.
   */
  Collection<String> selectorOption(String option);
}
