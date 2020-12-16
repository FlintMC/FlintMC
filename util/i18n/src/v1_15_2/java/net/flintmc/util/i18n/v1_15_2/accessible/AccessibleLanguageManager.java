package net.flintmc.util.i18n.v1_15_2.accessible;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
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
