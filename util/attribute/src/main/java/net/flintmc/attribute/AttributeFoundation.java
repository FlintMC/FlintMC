package net.flintmc.attribute;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.attribute.collect.AttributeFoundationMap;
import net.flintmc.attribute.modifier.AttributeModifier;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.Set;
import java.util.UUID;

/**
 * Represents a foundation for attributes.
 */
public interface AttributeFoundation {

  /**
   * Retrieves the attribute of the foundation.
   *
   * @return The foundation's attribute.
   */
  Attribute getAttribute();

  /**
   * Retrieves the value of the foundation.
   *
   * @return The foundation attribute's value.
   */
  double getFoundationValue();

  /**
   * Changes the value of the foundation.
   *
   * @param foundationValue The new foundation value.
   */
  void setFoundationValue(double foundationValue);

  /**
   * Retrieves a collection with all attribute modifiers with the given operation of the attribute foundation.
   *
   * @param operation The operation of attribute modifiers.
   * @return A collection with all attribute modifiers with the given operation.
   */
  Set<AttributeModifier> getAttributeModifiers(AttributeModifierOperation operation);

  /**
   * Retrieves a collection with all attribute modifiers of the attribute foundation.
   *
   * @return A collection with all attribute modifiers.
   */
  Set<AttributeModifier> getAttributeModifiers();

  /**
   * Whether the attribute foundation has a modifier.
   *
   * @param uniqueId The unique identifier of a modifier.
   * @return {@code true} if the attribute foundation has a modifier, otherwise {@code false}.
   */
  boolean hasModifier(UUID uniqueId);

  /**
   * Retrieves a modifier with the given unique identifier.
   *
   * @param uniqueId The unique identifier of a modifier.
   * @return A modifier with the given unique identifier or {@code null}.
   */
  AttributeModifier getModifier(UUID uniqueId);

  /**
   * Applied a new modifier to attribute foundation.
   *
   * @param modifier The modifier to be applied.
   */
  void applyModifier(AttributeModifier modifier);

  /**
   * Removes a modifier with the given attribute modifier.
   *
   * @param modifier The attribute modifier to be removed.
   */
  void removeModifier(AttributeModifier modifier);

  /**
   * Removes a modifier with the given unique identifier of the attribute foundation.
   *
   * @param uniqueId The unique identifier of a modifier.
   */
  void removeModifier(UUID uniqueId);

  /**
   * Removes all modifiers of the attribute foundation.
   */
  void removeAllModifiers();

  /**
   * Retrieves the value of the {@link #getAttribute()}.
   *
   * @return The attribute's value.
   */
  double getValue();

  /**
   * A factory class for creating {@link AttributeFoundation}'s.
   */
  @AssistedFactory(AttributeFoundation.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeFoundation} with the given parameters.
     *
     * @param attributeFoundationMap The attribute foundation map for the attribute foundation.
     * @param attribute              The attribute for the attribute foundation.
     * @return
     */
    AttributeFoundation create(
            @Assisted("attributeMap") AttributeFoundationMap attributeFoundationMap,
            @Assisted("attribute") Attribute attribute);
  }

}
