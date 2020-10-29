package net.flintmc.attribute.modifier;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.attribute.AttributeModifierOperation;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.UUID;

public interface AttributeModifier {

  /**
   * Retrieves the name of the attribute modifier.
   *
   * @return The attribute modifier's name.
   */
  String getName();

  /**
   * Retrieves the unique identifier of the attribute modifier.
   *
   * @return The attribute modifier's unique identifier.
   */
  UUID getUniqueId();

  /**
   * Retrieves the operation of the attribute modifier.
   *
   * @return The attribute modifier's operation.
   */
  AttributeModifierOperation getOperation();

  /**
   * Retrieves the amount of the attribute modifier.
   *
   * @return The attribute modifier's amount.
   */
  double getAmount();

  /**
   * Whether the attribute modifier is saved.
   *
   * @return {@code true} if the attribute modifier is saved, otherwise {@code false}.
   */
  boolean isSaved();

  /**
   * Changes whether the attribute modifier is saved.
   *
   * @param saved {@code true} if the attribute modifier should be saved, otherwise {@code false}.
   * @return This attribute modifier, for chaining.
   */
  AttributeModifier setSaved(boolean saved);

  /**
   * A factory class for creating {@link AttributeModifier}'s.
   */
  @AssistedFactory(AttributeModifier.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeModifier} with the given parameters.
     *
     * @param name      The name of the attribute modifier.
     * @param uniqueId  The unique identifier of the attribute modifier.
     * @param amount    The amount of the attribute modifier.
     * @param operation The operation of the attribute modifier.
     * @return A created attribute modifier.
     */
    AttributeModifier create(
            @Assisted("name") String name,
            @Assisted("uniqueId") UUID uniqueId,
            @Assisted("amount") double amount,
            @Assisted("operation") AttributeModifierOperation operation);

  }

}
