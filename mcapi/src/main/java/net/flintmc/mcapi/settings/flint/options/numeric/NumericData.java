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

package net.flintmc.mcapi.settings.flint.options.numeric;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import java.util.Map;

public interface NumericData extends SettingData {

  double getMinValue();

  void setMinValue(double minValue);

  double getMaxValue();

  void setMaxValue(double maxValue);

  int getDecimalPlaces();

  void setDecimalPlaces(int decimalPlaces);

  ChatComponent getOverriddenDisplay(double value);

  void setOverriddenDisplay(double value, ChatComponent display);

  Map<Double, ChatComponent> getOverriddenDisplays();

  @AssistedFactory(NumericData.class)
  interface Factory {

    NumericData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("minValue") double minValue,
        @Assisted("maxValue") double maxValue,
        @Assisted("decimalPlaces") int decimalPlaces,
        @Assisted("overriddenDisplays") Map<Double, ChatComponent> overriddenDisplays);

  }

}
