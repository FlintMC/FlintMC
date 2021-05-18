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

package net.flintmc.mcapi.v1_16_5.server.event.shadow;

import java.util.function.Consumer;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.text.ITextComponent;

/**
 * Shadow implementation of the {@link net.minecraft.client.network.login.ClientLoginNetHandler} in
 * minecraft with a getter and setter for the status message consumer.
 */
@Shadow("net.minecraft.client.network.login.ClientLoginNetHandler")
public interface ClientLoginNetHandlerShadow {

  /**
   * Retrieves the currently set consumer for the status message.
   *
   * @return The non-null status message consumer
   */
  @FieldGetter("statusMessageConsumer")
  Consumer<ITextComponent> getStatusMessageConsumer();

  /**
   * Modifies the (usually final) status message consumer of this instance.
   *
   * @param statusMessageConsumer The new non-null status message consumer
   */
  @FieldSetter("statusMessageConsumer")
  void setStatusMessageConsumer(Consumer<ITextComponent> statusMessageConsumer);

}
