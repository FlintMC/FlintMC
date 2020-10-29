package net.flintmc.attribute;

/**
 * An enumeration that representing all attribute modifier operations.
 */
public enum AttributeModifierOperation {

  ADDITION(0),
  MULTIPLY_BASE(1),
  MULTIPLY_TOTAL(2);

  private static final AttributeModifierOperation[] VALUES = new AttributeModifierOperation[]{
          ADDITION,
          MULTIPLY_BASE,
          MULTIPLY_TOTAL
  };
  private final int identifier;

  AttributeModifierOperation(int identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves an attribute modifier operation by the given identifier.
   *
   * @param identifier The identifier of an attribute modifier operation.
   * @return An attribute modifier operation.
   * @throws IllegalArgumentException Is thrown if no identifier with an operator is possible.
   */
  public static AttributeModifierOperation byIdentifier(int identifier) {
    if (identifier >= 0 && identifier < VALUES.length) {
      return VALUES[identifier];
    } else {
      throw new IllegalArgumentException("No operation with the identifier " + identifier);
    }
  }

  /**
   * Retrieves the identifier of the attribute modifier operation.
   *
   * @return The attribute modifier operation's identifier.
   */
  public int getIdentifier() {
    return identifier;
  }

}
