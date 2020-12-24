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

package net.flintmc.mcapi.internal.player.gameprofile.property;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;

import java.security.*;
import java.util.Base64;

/** An implementation of {@link Property} */
@Implement(Property.class)
public class DefaultProperty implements Property {

  private final String name;
  private final String value;
  private final String signature;

  @AssistedInject
  private DefaultProperty(@Assisted("name") String name, @Assisted("value") String value) {
    this(name, value, null);
  }

  @AssistedInject
  private DefaultProperty(
      @Assisted("name") String name,
      @Assisted("value") String value,
      @Assisted("signature") String signature) {
    this.name = name;
    this.value = value;
    this.signature = signature;
  }

  /**
   * Retrieves the name of this property
   *
   * @return The property name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Retrieves the value of this property
   *
   * @return The property value
   */
  @Override
  public String getValue() {
    return this.value;
  }

  /**
   * Retrieves the signature of this property
   *
   * @return The property signature
   */
  @Override
  public String getSignature() {
    return this.signature;
  }

  /**
   * Whether this property has a signature.
   *
   * @return {@code true} if this property has a signature, otherwise {@code false}
   */
  @Override
  public boolean hasSignature() {
    return this.signature != null;
  }

  /**
   * Whether this property has a valid signature.
   *
   * @param publicKey The public key to signature verification
   * @return {@code true} if this property has a valid signature, otherwise {@code false}
   */
  @Override
  public boolean isSignatureValid(PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initVerify(publicKey);
      signature.update(this.value.getBytes());
      return signature.verify(Base64.getDecoder().decode(this.signature));
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      e.printStackTrace();
    }
    return false;
  }
}
