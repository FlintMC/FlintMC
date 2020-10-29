package net.flintmc.attribute.attributes;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.attribute.Attribute;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents a ranged attribute
 */
public interface RangedAttribute extends Attribute {

  /**
   * Retrieves the description of the ranged attribute.
   *
   * @return The ranged attribute's description.
   */
  String getDescription();

  /**
   * Changes the description of the ranged attribute.
   *
   * @param description The new description.
   * @return The changed description of the ranged attribute.
   */
  RangedAttribute setDescription(String description);

  /**
   * A factory class for creating {@link RangedAttribute}'s.
   */
  @AssistedFactory(RangedAttribute.class)
  interface Factory {

    /**
     * Creates a new {@link RangedAttribute} with the given parameters.
     *
     * @param name         The name of the ranged attribute.
     * @param defaultValue The default value of the ranged attribute.
     * @param minimumValue The minimum value of the ranged attribute.
     * @param maximumValue The maximum value of the ranged attribute.
     * @return A created ranged attribute.
     */
    RangedAttribute create(
            @Assisted("name") String name,
            @Assisted("defaultValue") double defaultValue,
            @Assisted("minimumValue") double minimumValue,
            @Assisted("maximumValue") double maximumValue);

    /**
     * Creates a new {@link RangedAttribute} with the given parameters.
     *
     * @param name         The name of the ranged attribute.
     * @param defaultValue The default value of the ranged attribute.
     * @param description  The description of the ranged attribute.
     * @param minimumValue The minimum value of the ranged attribute.
     * @param maximumValue The maximum value of the ranged attribute.
     * @return A created ranged attribute.
     */
    RangedAttribute create(
            @Assisted("name") String name,
            @Assisted("defaultValue") double defaultValue,
            @Assisted("description") String description,
            @Assisted("minimumValue") double minimumValue,
            @Assisted("maximumValue") double maximumValue);

  }

}
