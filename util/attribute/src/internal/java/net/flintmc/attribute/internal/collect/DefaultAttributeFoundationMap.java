package net.flintmc.attribute.internal.collect;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import net.flintmc.attribute.Attribute;
import net.flintmc.attribute.AttributeFoundation;
import net.flintmc.attribute.collect.AttributeFoundationMap;
import net.flintmc.attribute.modifier.AttributeModifier;
import net.flintmc.framework.inject.implement.Implement;

import java.util.Collection;
import java.util.Map;

@Implement(AttributeFoundationMap.class)
public class DefaultAttributeFoundationMap<T extends AttributeFoundation> implements AttributeFoundationMap<T> {

  protected final Map<Attribute, T> attributes;
  protected final Map<String, T> attributesByName;

  @Inject
  public DefaultAttributeFoundationMap() {
    this.attributes = Maps.newHashMap();
    this.attributesByName = Maps.newHashMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getAttributeFoundation(Attribute attribute) {
    return this.attributes.get(attribute);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getAttributeFoundationByName(String attributeName) {
    return this.attributesByName.get(attributeName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T registerAttribute(Attribute attribute) {
    if (this.attributesByName.containsKey(attribute.getName())) {
      throw new IllegalArgumentException("The attribute" + attribute.getName() + " is already registered!");
    } else {
      T attributeFoundation = this.createInstance(attribute);
      this.attributesByName.put(attribute.getName(), attributeFoundation);
      this.attributes.put(attribute, attributeFoundation);

      return attributeFoundation;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
    for (Map.Entry<String, AttributeModifier> entry : modifiers.entries()) {
      AttributeFoundation attributeFoundation = this.getAttributeFoundationByName(entry.getKey());

      if (attributeFoundation != null) {
        attributeFoundation.removeModifier(entry.getValue());
        attributeFoundation.applyModifier(entry.getValue());
      }

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeModifiers(Multimap<String, AttributeModifier> modifiers) {
    for (Map.Entry<String, AttributeModifier> entry : modifiers.entries()) {
      AttributeFoundation attributeFoundation = this.getAttributeFoundationByName(entry.getKey());

      if (attributeFoundation != null) {
        attributeFoundation.removeModifier(entry.getValue());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<T> getAllAttributes() {
    return this.attributesByName.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onAttributeModified(AttributeFoundation foundation) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T createInstance(Attribute attribute) {
    return null;
  }

}
