package net.flintmc.util.internal.attribute;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.attribute.AttributeModifier;
import net.flintmc.util.attribute.AttributeOperation;

@Implement(AttributeModifier.class)
public class DefaultAttributeModifier implements AttributeModifier {

  private final UUID uniqueId;
  private final String name;
  private final float amount;
  private final AttributeOperation operation;

  @AssistedInject
  public DefaultAttributeModifier(
      @Assisted("name") String name,
      @Assisted("amount") float amount,
      @Assisted("operation") AttributeOperation operation) {
    this(UUID.randomUUID(), name, amount, operation);
  }

  @AssistedInject
  public DefaultAttributeModifier(
      @Assisted("uniqueId") UUID uniqueId,
      @Assisted("name") String name,
      @Assisted("amount") float amount,
      @Assisted("operation") AttributeOperation operation) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.amount = amount;
    this.operation = operation;
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public float getAmount() {
    return this.amount;
  }

  /** {@inheritDoc} */
  @Override
  public AttributeOperation getOperation() {
    return this.operation;
  }
}
