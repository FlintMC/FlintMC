package net.labyfy.component.nbt;

public enum NBTType {

  TAG_END(0),
  TAG_BYTE(1),
  TAG_SHORT(2),
  TAG_INT(3),
  TAG_LONG(4),
  TAG_FLOAT(5),
  TAG_DOUBLE(6),
  TAG_BYTE_ARRAY(7),
  TAG_STRING(8),
  TAG_LIST(9),
  TAG_COMPOUND(10),
  TAG_INT_ARRAY(11),
  TAG_LONG_ARRAY(12),
  ;

  private final int identifier;

  NBTType(int identifier) {
    this.identifier = identifier;
  }

  public static NBTType getNbt(int identifier) {
    for (NBTType value : values()) {
      if (value.identifier == identifier) {
        return value;
      }
    }
    return null;
  }

  public int getIdentifier() {
    return identifier;
  }
}
