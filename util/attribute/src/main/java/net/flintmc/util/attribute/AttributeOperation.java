package net.flintmc.util.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration that representing all attribute operations.
 */
public enum AttributeOperation {
  ADDITION(0),
  MULTIPLY_BASE(1),
  MULTIPLY_TOTAL(2);

  private static final Map<Integer, AttributeOperation> BY_IDENTIFIER = new HashMap<>();

  static {
    for (AttributeOperation value : values()) {
      BY_IDENTIFIER.put(value.identifier, value);
    }
  }

  private final int identifier;

  AttributeOperation(int identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves an attribute operation with the given {@code identifier}.
   *
   * @param identifier The identifier of the attribute operation.
   * @return An attribute operation or {@code null}.
   */
  public static AttributeOperation fromIdentifier(int identifier) {
    return BY_IDENTIFIER.get(identifier);
  }

  /**
   * Retrieves the identifier of this operation.
   *
   * @return The operation's identifier.
   */
  public int getIdentifier() {
    return identifier;
  }
}
