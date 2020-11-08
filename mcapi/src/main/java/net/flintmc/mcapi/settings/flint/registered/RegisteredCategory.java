package net.flintmc.mcapi.settings.flint.registered;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.settings.flint.annotation.ui.Category;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;

/**
 * Represents a category of settings in the {@link SettingsProvider}.
 *
 * @see DefineCategory
 * @see Category
 */
public interface RegisteredCategory {

  /**
   * Retrieves the name of this category, unique per {@link SettingsProvider}.
   *
   * @return The non-null registry name of this category
   */
  String getRegistryName();

  /**
   * Retrieves the display name with all formats/colors as it will be displayed.
   *
   * @return The non-null display name of this category
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the description with all formats/colors as it will be displayed. If no description is set, this will be a
   * {@link TextComponent} with an empty text.
   *
   * @return The non-null description of this category
   */
  ChatComponent getDescription();

  /**
   * Retrieves the URL to the icon of this category. If no icon is set, this will be an empty string.
   *
   * @return The non-null URL to the icon of this category
   */
  String getIconUrl();

  /**
   * Factory for the {@link RegisteredCategory}.
   */
  @AssistedFactory(RegisteredCategory.class)
  interface Factory {

    /**
     * Creates a new {@link RegisteredCategory} which can be registered with {@link
     * SettingsProvider#registerCategory(RegisteredCategory)}.
     *
     * @param registryName The non-null name of the new category, unique per {@link SettingsProvider}
     * @param displayName  The non-null display name of the new category as it will be displayed
     * @param description  The non-null description of the new category as it will be displayed, if no description is
     *                     available, this should be a {@link TextComponent} with an empty text
     * @param iconUrl      The non-null URL to the icon of the new category, if no icon is set, this should be an empty
     *                     string
     * @return The new non-null {@link RegisteredCategory}
     */
    RegisteredCategory create(@Assisted("registryName") String registryName,
                              @Assisted("displayName") ChatComponent displayName,
                              @Assisted("description") ChatComponent description,
                              @Assisted("iconUrl") String iconUrl);

  }

}
