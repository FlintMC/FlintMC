package net.flintmc.attribute.internal.collect;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import net.flintmc.attribute.Attribute;
import net.flintmc.attribute.AttributeFoundation;
import net.flintmc.attribute.attributes.RangedAttribute;
import net.flintmc.attribute.collect.AttributeMap;
import net.flintmc.attribute.internal.modifier.DefaultModifiableAttributeFoundation;
import net.flintmc.framework.inject.implement.Implement;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Implement(AttributeMap.class)
public class DefaultAttributeMap extends DefaultAttributeFoundationMap<DefaultModifiableAttributeFoundation>
        implements AttributeMap<DefaultModifiableAttributeFoundation> {

  protected final Map<String, AttributeFoundation> instancesByName;
  private final Set<AttributeFoundation> instances;

  private final AttributeFoundation.Factory attributeFoundationFactory;

  @Inject
  public DefaultAttributeMap(AttributeFoundation.Factory attributeFoundationFactory) {
    this.instances = Sets.newHashSet();
    this.instancesByName = Maps.newHashMap();

    this.attributeFoundationFactory = attributeFoundationFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModifiableAttributeFoundation getAttributeFoundation(Attribute attribute) {
    return super.getAttributeFoundation(attribute);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModifiableAttributeFoundation getAttributeFoundationByName(String attributeName) {
    AttributeFoundation attributeFoundation = super.getAttributeFoundationByName(attributeName);
    if (attributeFoundation == null) {
      attributeFoundation = this.instancesByName.get(attributeName);
    }

    return (DefaultModifiableAttributeFoundation) attributeFoundation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModifiableAttributeFoundation registerAttribute(Attribute attribute) {
    DefaultModifiableAttributeFoundation attributeFoundation = super.registerAttribute(attribute);

    if (attribute instanceof RangedAttribute && ((RangedAttribute) attribute).getDescription() != null) {
      this.instancesByName.put(((RangedAttribute) attribute).getDescription(), attributeFoundation);
    }

    return attributeFoundation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModifiableAttributeFoundation createInstance(Attribute attribute) {
    return (DefaultModifiableAttributeFoundation) this.attributeFoundationFactory.create(this, attribute);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onAttributeModified(AttributeFoundation foundation) {
    if (foundation.getAttribute().shouldWatch()) {
      this.instances.add(foundation);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AttributeFoundation> getInstances() {
    return this.instances;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<AttributeFoundation> getWatchedAttributes() {
    Set<AttributeFoundation> foundations = Sets.newHashSet();

    for (DefaultModifiableAttributeFoundation allAttribute : this.getAllAttributes()) {
      if (allAttribute.getAttribute().shouldWatch()) {
        foundations.add(allAttribute);
      }
    }

    return foundations;
  }
}
