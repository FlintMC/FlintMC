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

package net.flintmc.mcapi.player.gameprofile.property;

import java.security.PublicKey;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the property of a game profile.
 */
public interface Property {

  /**
   * Retrieves the name of this property
   *
   * @return The property name
   */
  String getName();

  /**
   * Retrieves the value of this property
   *
   * @return The property value
   */
  String getValue();

  /**
   * Retrieves the signature of this property
   *
   * @return The property signature
   */
  String getSignature();

  /**
   * Whether this property has a signature.
   *
   * @return {@code true} if this property has a signature, otherwise {@code false}
   */
  boolean hasSignature();

  /**
   * Whether this property has a valid signature.
   *
   * @param publicKey The public key to signature verification
   * @return {@code true} if this property has a valid signature, otherwise {@code false}
   */
  boolean isSignatureValid(PublicKey publicKey);

  /**
   * A factory class for the {@link Property}.
   */
  @AssistedFactory(Property.class)
  interface Factory {

    /**
     * Creates a new {@link Property} with the given name and value.
     *
     * @param name  The name of the property
     * @param value The value of the property.
     * @return A created property.
     */
    Property create(@Assisted("name") String name, @Assisted("value") String value);

    /**
     * Creates a new {@link Property} with the given parameters.
     *
     * @param name      The name of the property
     * @param value     The value of the property.
     * @param signature The signature of the property.
     * @return A created property.
     */
    Property create(
        @Assisted("name") String name,
        @Assisted("value") String value,
        @Assisted("signature") String signature);
  }
}
