package net.flintmc.util.i18n.v1_15_2;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.i18n.I18n;

/** 1.15.2 implementation of the {@link I18n}. */
@Implement(value = I18n.class, version = "1.15.2")
public class VersionedI18n implements I18n {

  /** {@inheritDoc} */
  @Override
  public String translate(String translationKey, Object... parameters) {
    return net.minecraft.client.resources.I18n.format(translationKey, parameters);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasTranslation(String key) {
    return net.minecraft.client.resources.I18n.hasKey(key);
  }
}
