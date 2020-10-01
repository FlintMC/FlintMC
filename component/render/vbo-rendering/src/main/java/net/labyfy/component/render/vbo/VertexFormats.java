package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.primitive.InjectionHolder;

public enum VertexFormats implements VertexFormat {
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
  POS3F_RGBA_UV {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.COLOR_RGBA)
          .addAttribute(VertexAttributes.TEXTURE_UV)
          .build();
    }
  };

  private final VertexFormat format;

  abstract VertexFormat createCopy();

  VertexFormats() {
    this.format = createCopy();
  }

  @Override
  public int getAttributeCount() {
    return this.format.getAttributeCount();
  }

  @Override
  public int getVertexSize() {
    return this.format.getAttributeCount();
  }

  @Override
  public int getID() {
    return this.format.getID();
  }

  @Override
  public void bind() {
    this.format.bind();
  }

  @Override
  public void unbind() {
    this.format.unbind();
  }
}
