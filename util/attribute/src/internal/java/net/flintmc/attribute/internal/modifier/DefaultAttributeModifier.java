package net.flintmc.attribute.internal.modifier;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.attribute.AttributeModifierOperation;
import net.flintmc.attribute.modifier.AttributeModifier;
import net.flintmc.framework.inject.implement.Implement;

import java.util.UUID;

@Implement(AttributeModifier.class)
public class DefaultAttributeModifier implements AttributeModifier {

  private final String name;
  private final UUID uniqueId;
  private final double amount;
  private final AttributeModifierOperation operation;
  private boolean saved;

  @AssistedInject
  public DefaultAttributeModifier(
          @Assisted("name") String name,
          @Assisted("uniqueId") UUID uniqueId,
          @Assisted("amount") double amount,
          @Assisted("operation") AttributeModifierOperation operation) {
    this.name = name;
    this.uniqueId = uniqueId;
    this.amount = amount;
    this.operation = operation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeModifierOperation getOperation() {
    return this.operation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getAmount() {
    return this.amount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSaved() {
    return this.saved;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeModifier setSaved(boolean saved) {
    this.saved = saved;
    return this;
  }
}
