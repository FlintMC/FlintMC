package net.flintmc.attribute.collect;

import com.google.common.collect.Multimap;
import net.flintmc.attribute.Attribute;
import net.flintmc.attribute.AttributeFoundation;
import net.flintmc.attribute.modifier.AttributeModifier;

import java.util.Collection;

/**
 * Represents an attribute foundation map.
 *
 * @param <T> An implementation of an attribute.
 */
public interface AttributeFoundationMap<T extends AttributeFoundation> {

  /**
   * Retrieves an attribute foundation of the given attribute.
   *
   * @param attribute An attribute of a foundation.
   * @return The attribute foundation or {@code null}.
   */
  T getAttributeFoundation(Attribute attribute);

  /**
   * Retrieves an attribute foundation by the given attribute name.
   *
   * @param attributeName The name of an attribute.
   * @return The attribute foundation or {@code null}.
   */
  T getAttributeFoundationByName(String attributeName);

  /**
   * Registers a new attribute to the foundation.
   *
   * @param attribute The attribute to be registered.
   * @return The attribute foundation.
   */
  T registerAttribute(Attribute attribute);

  /**
   * Applied new modifiers to the map.
   *
   * @param modifiers The modifiers to be applied.
   */
  void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers);

  /**
   * Removes modifiers from the map.
   *
   * @param modifiers The modifiers to be removed.
   */
  void removeModifiers(Multimap<String, AttributeModifier> modifiers);

  /**
   * Retrieves a collection with all attributes of the foundation.
   *
   * @return A collection with all attributes of the foundation.
   */
  Collection<T> getAllAttributes();

  /**
   * Is fired when an attribute foundation is modified.
   *
   * @param foundation The modified attributes foundation.
   */
  void onAttributeModified(AttributeFoundation foundation);

  T createInstance(Attribute attribute);

}
