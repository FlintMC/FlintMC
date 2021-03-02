/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.vbo;

import net.flintmc.framework.inject.primitive.InjectionHolder;

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
  },
  POS3F_RGB {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.COLOR_RGB)
          .build();
    }
  },
  POS3F_RGBA {
    @Override
    VertexFormat createCopy() {
      return InjectionHolder.getInjectedInstance(VertexFormatBuilder.class)
          .addAttribute(VertexAttributes.POSITION3F)
          .addAttribute(VertexAttributes.COLOR_RGBA)
          .build();
    }
  };

  private final VertexFormat format;

  VertexFormats() {
    this.format = createCopy();
  }

  /**
   * @return a copy of the vertex format. Should usually not be called as vertex formats can usually
   *     be reused.
   */
  abstract VertexFormat createCopy();

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
