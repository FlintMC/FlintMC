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

package net.flintmc.mcapi.settings.flint.options.text.string;

import java.util.Collection;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

public interface StringData extends SettingData {

  int getMaxLength();

  void setMaxLength(int maxLength);

  String getPrefix();

  void setPrefix(String prefix);

  String getSuffix();

  void setSuffix(String suffix);

  String getPlaceholder();

  void setPlaceholder(String placeholder);

  Collection<StringRestriction> getRestrictions();

  void addRestriction(StringRestriction restriction);

  void removeRestriction(StringRestriction restriction);

  @AssistedFactory(StringData.class)
  interface Factory {

    StringData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("prefix") String prefix,
        @Assisted("suffix") String suffix,
        @Assisted("placeholder") String placeholder,
        @Assisted("maxLength") int maxLength,
        @Assisted("restrictions") Collection<StringRestriction> restrictions);

    StringData create(@Assisted("setting") RegisteredSetting setting);

  }

}
