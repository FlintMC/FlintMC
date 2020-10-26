package net.labyfy.component.i18n.accessible;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;
import net.minecraft.client.resources.Locale;

/**
 * A shadow interface for the Minecraft LanguageManager.
 */
@Shadow("net.minecraft.client.resources.LanguageManager")
public interface AccessibleLanguageManager {

  /**
   * Retrieves the current locale.
   *
   * @return The current locale.
   */
  @FieldGetter("CURRENT_LOCALE")
  Locale getLocale();

}
