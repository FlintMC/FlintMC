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

package net.flintmc.util.i18n.v1_16_4;

import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.i18n.Localization;
import net.flintmc.util.i18n.v1_16_4.shadow.AccessibleClientLanguageMap;

@Singleton
@Implement(value = Localization.class, version = "1.16.4")
public class VersionedLocalization implements Localization {

  private final AccessibleClientLanguageMap clientLanguageMap;

  public VersionedLocalization(AccessibleClientLanguageMap clientLanguageMap) {
    this.clientLanguageMap = clientLanguageMap;
  }

  /** {@inheritDoc} */
  @Override
  public void add(String key, String translation) {
    this.clientLanguageMap.getTranslations().put(key, translation);
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, String> getProperties() {
    return this.clientLanguageMap.getTranslations();
  }
}
