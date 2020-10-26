package net.labyfy.component.i18n;

import net.labyfy.component.inject.implement.Implement;

/**
 * 1.15.2 implementation of the {@link I18n}.
 */
@Implement(value = I18n.class, version = "1.15.2")
public class VersionedI18n implements I18n {

  /**
   * {@inheritDoc}
   */
  @Override
  public String translate(String translationKey, Object... parameters) {
    return net.minecraft.client.resources.I18n.format(translationKey, parameters);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasTranslation(String key) {
    return net.minecraft.client.resources.I18n.hasKey(key);
  }
}
