package net.flintmc.util.attribute;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents an attribute modifier.
 */
public interface AttributeModifier {

  /**
   * Retrieves the unique identifier of this attribute modifier.
   *
   * @return The attribute modifier's unique identifier.
   */
  UUID getUniqueId();

  /**
   * Retrieves the name of this attribute modifier.
   *
   * @return The attribute modifier's name.
   */
  String getName();

  /**
   * Retrieves the value of this attribute modifier.
   *
   * @return The attribute modifier's value.
   */
  float getAmount();

  /**
   * Retrieves the operation of this attribute modifier.
   *
   * @return The attribute modifier's operation.
   */
  AttributeOperation getOperation();

  /**
   * A factory for {@link AttributeModifier}.
   */
  @AssistedFactory(AttributeModifier.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeModifier} with the given {@code name}, {@code amount} and the
     * {@code operation}.
     *
     * @param name      The name of this attribute modifier.
     * @param amount    The amount of this attribute modifier.
     * @param operation The operation to apply this attribute modifier with.
     * @return A created attribute modifier.
     */
    AttributeModifier create(String name, float amount, AttributeOperation operation);

    /**
     * Creates a new {@link AttributeModifier} with the given {@code uniqueId}, {@code name}, {@code
     * amount} and the {@code operation}.
     *
     * @param uniqueId  The unique identifier of this attribute modifier.
     * @param name      The name of this attribute modifier.
     * @param amount    The amount of this attribute modifier.
     * @param operation The operation to apply this attribute modifier with.
     * @return A created attribute modifier.
     */
    AttributeModifier create(UUID uniqueId, String name, float amount,
        AttributeOperation operation);

  }

}
