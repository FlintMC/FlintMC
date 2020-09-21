package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexFormatElement {
  <T> T getHandle();

  int getAmount();

  Type getType();

  VertexFormatElementType getId();

  enum Type {
    FLOAT(4, "Float", 5126),
    UBYTE(1, "Unsigned Byte", 5121),
    BYTE(1, "Byte", 5120),
    USHORT(2, "Unsigned Short", 5123),
    SHORT(2, "Short", 5122),
    UINT(4, "Unsigned Int", 5125),
    INT(4, "Int", 5124);

    private final int size;
    private final String displayName;
    private final int glConstant;

    Type(int sizeIn, String displayNameIn, int glConstantIn) {
      this.size = sizeIn;
      this.displayName = displayNameIn;
      this.glConstant = glConstantIn;
    }

    public int getSize() {
      return this.size;
    }

    public String getDisplayName() {
      return this.displayName;
    }

    public int getGlConstant() {
      return this.glConstant;
    }
  }

  @AssistedFactory(VertexFormatElement.class)
  interface Factory {

    VertexFormatElement create(
        @Assisted("index") int index,
        @Assisted VertexFormatUsage usage,
        @Assisted("id") VertexFormatElementType id,
        @Assisted Type type,
        @Assisted("amount") int amount
    );

  }
}
