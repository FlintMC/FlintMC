package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.primitive.InjectionHolder;

import java.util.List;

/** Lists predefined vertex formats. */
public enum VertexFormats implements EnumeratedVertexFormat {
  /**
   * A vertex format that contains a homogenous 3D position, a RGB color and texture coordinates.
   */
  POS4F_RGB_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION4F)
          .addAttribute(VertexAttributes.COLOR_RGB)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  },
  /** A vertex format that contains a 3D position, a RGB color and texture coordinates. */
  POS3F_RGB_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.COLOR_RGB)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  },
  /**
   * A vertex format that contains a homogenous 3D position, a RGBA color and texture coordinates.
   */
  POS4F_RGBA_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION4F)
          .addAttribute(VertexAttributes.COLOR_RGBA)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  },
  /** A vertex format that contains a 3D position, a RGBA color and texture coordinates. */
  POS3F_RGBA_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.COLOR_RGBA)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  },
  /** A vertex format that contains a 3D position and texture coordinates. */
  POS3F_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  };

  private final VertexFormat format;

  /**
   * @return a copy of the vertex format. Should usually not be called as vertex formats can usually
   *     be reused.
   */
  abstract VertexFormat createCopy();

  VertexFormats() {
    this.format = createCopy();
  }

  /** {@inheritDoc} */
  @Override
  public int getAttributeCount() {
    return this.format.getAttributeCount();
  }

  /** {@inheritDoc} */
  @Override
  public int getVertexSize() {
    return this.format.getVertexSize();
  }

  /** {@inheritDoc} */
  @Override
  public List<VertexAttribute> getAttributes() {
    return this.format.getAttributes();
  }

  /** {@inheritDoc} */
  @Override
  public int createVAO() {
    return this.format.createVAO();
  }

  /** {@inheritDoc} */
  @Override
  public void pushToGPU(int vao) {
    this.format.pushToGPU(vao);
  }
}
