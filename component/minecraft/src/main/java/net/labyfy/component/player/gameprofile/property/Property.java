package net.labyfy.component.player.gameprofile.property;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.security.PublicKey;

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

  @AssistedFactory(Property.class)
  interface Factory {

    Property create(@Assisted("name") String name, @Assisted("value") String value);

    Property create(@Assisted("name") String name, @Assisted("value") String value, @Assisted("signature") String signature);

  }

}
