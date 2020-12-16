package net.flintmc.util.internal.attribute;

import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.attribute.Attribute;
import net.flintmc.util.attribute.AttributeMap;

@Implement(AttributeMap.class)
public class DefaultAttributeMap implements AttributeMap {

  private final Map<String, Attribute> attributes;

  @AssistedInject
  public DefaultAttributeMap() {
    this.attributes = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeMap register(Attribute attribute) {
    this.attributes.put(attribute.getKey(), attribute);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute get(String key) {
    return this.attributes.get(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute[] values() {
    return this.attributes.values().toArray(new Attribute[0]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute[] sharedValues() {
    return this.attributes.values().stream().filter(Attribute::isShared).toArray(Attribute[]::new);
  }
}
