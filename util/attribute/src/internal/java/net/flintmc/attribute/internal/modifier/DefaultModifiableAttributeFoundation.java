package net.flintmc.attribute.internal.modifier;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.attribute.Attribute;
import net.flintmc.attribute.AttributeFoundation;
import net.flintmc.attribute.collect.AttributeFoundationMap;
import net.flintmc.attribute.modifier.AttributeModifier;
import net.flintmc.attribute.AttributeModifierOperation;
import net.flintmc.framework.inject.implement.Implement;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Implement(AttributeFoundation.class)
public class DefaultModifiableAttributeFoundation implements AttributeFoundation {

  private final AttributeFoundationMap<AttributeFoundation> defaultAttributeFoundationMap;
  private final Attribute genericAttribute;

  private final Map<AttributeModifierOperation, Set<AttributeModifier>> operations;
  private final Map<String, Set<AttributeModifier>> byName;
  private final Map<UUID, AttributeModifier> byUniqueId;

  private double foundationValue;
  private double cachedValue;

  private boolean needsUpdate;

  @AssistedInject
  public DefaultModifiableAttributeFoundation(
          @Assisted("attributeMap") AttributeFoundationMap defaultAttributeFoundationMap,
          @Assisted("attribute") Attribute genericAttribute) {
    this.operations = Maps.newEnumMap(AttributeModifierOperation.class);
    this.byName = Maps.newHashMap();
    this.byUniqueId = Maps.newHashMap();

    this.defaultAttributeFoundationMap = defaultAttributeFoundationMap;
    this.genericAttribute = genericAttribute;
    this.foundationValue = genericAttribute.getDefaultValue();

    this.needsUpdate = true;

    for (AttributeModifierOperation operation : AttributeModifierOperation.values()) {
      this.operations.put(operation, Sets.newHashSet());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute getAttribute() {
    return this.genericAttribute;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getFoundationValue() {
    return this.foundationValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFoundationValue(double foundationValue) {
    this.foundationValue = foundationValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AttributeModifier> getAttributeModifiers(AttributeModifierOperation operation) {
    return this.operations.get(operation);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AttributeModifier> getAttributeModifiers() {
    Set<AttributeModifier> modifiers = Sets.newHashSet();

    for (AttributeModifierOperation operation : AttributeModifierOperation.values()) {
      modifiers.addAll(this.getAttributeModifiers(operation));
    }

    return modifiers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasModifier(UUID uniqueId) {
    return this.byUniqueId.containsKey(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeModifier getModifier(UUID uniqueId) {
    return this.byUniqueId.get(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyModifier(AttributeModifier modifier) {
    if (this.getModifier(modifier.getUniqueId()) != null) {
      throw new IllegalArgumentException("The modifier is already applied on this attribute!");
    } else {
      Set<AttributeModifier> modifiers = this.byName.computeIfAbsent(modifier.getName(), (set) -> Sets.newHashSet());

      this.operations.get(modifier.getOperation()).add(modifier);
      modifiers.add(modifier);

      this.byUniqueId.put(modifier.getUniqueId(), modifier);
      this.executeUpdate();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeModifier(AttributeModifier modifier) {
    for (AttributeModifierOperation operation : AttributeModifierOperation.values()) {
      this.operations.get(operation).remove(modifier);
    }

    Set<AttributeModifier> modifiers = this.byName.get(modifier.getName());

    if (modifiers != null) {
      modifiers.remove(modifier);
      if (modifiers.isEmpty()) {
        this.byName.remove(modifier.getName());
      }
    }


    this.byUniqueId.remove(modifier.getUniqueId());
    this.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeModifier(UUID uniqueId) {
    AttributeModifier attributeModifier = this.getModifier(uniqueId);
    if (attributeModifier != null) {
      this.removeModifier(attributeModifier);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeAllModifiers() {
    Collection<AttributeModifier> collection = this.getAttributeModifiers();
    if (collection != null) {
      for (AttributeModifier attributeModifier : collection) {
        this.removeModifier(attributeModifier);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getValue() {
    if (this.needsUpdate) {
      this.cachedValue = this.computeValue();
      this.needsUpdate = false;
    }

    return this.cachedValue;
  }

  private double computeValue() {
    double computeValue = this.getFoundationValue();

    for (AttributeModifier attributeModifier : this.getAttributeModifiers(AttributeModifierOperation.ADDITION)) {
      computeValue += attributeModifier.getAmount();
    }

    double compute = computeValue;

    for (AttributeModifier attributeModifier : this.getAttributeModifiers(AttributeModifierOperation.MULTIPLY_BASE)) {
      compute += computeValue * attributeModifier.getAmount();
    }

    for (AttributeModifier attributeModifier : this.getAttributeModifiers(AttributeModifierOperation.MULTIPLY_TOTAL)) {
      compute *= 1.0D + attributeModifier.getAmount();
    }

    return this.genericAttribute.clampValue(compute);
  }

  private void executeUpdate() {
    this.needsUpdate = true;
    this.defaultAttributeFoundationMap.onAttributeModified(this);
  }
}
