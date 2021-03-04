/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.i18n.v1_15_2;

import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.i18n.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;

/**
 * 1.15.2 implementation of the {@link I18n}.
 */
@Singleton
@Implement(value = I18n.class)
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

  @Override
  public Collection<String> getAvailableLanguages() {
    Collection<String> languages = new HashSet<>();

    for (Language language : Minecraft.getInstance().getLanguageManager().getLanguages()) {
      languages.add(language.toString());
    }

    return languages;
  }
}
