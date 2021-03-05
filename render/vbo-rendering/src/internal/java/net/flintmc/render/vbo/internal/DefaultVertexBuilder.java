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

package net.flintmc.render.vbo.internal;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.EnumeratedVertexFormat;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexAttributes;
import net.flintmc.render.vbo.VertexBufferObject;
import net.flintmc.render.vbo.VertexBuilder;

/**
 * {@inheritDoc}
 */
@Implement(VertexBuilder.class)
public class DefaultVertexBuilder implements VertexBuilder {

  private final VertexBufferObject vbo;

  private final DefaultAttributeValueHandler pos3fHandler;
  private final DefaultAttributeValueHandler pos4fHandler;
  private final DefaultAttributeValueHandler normalHandler;
  private final DefaultAttributeValueHandler rgbHandler;
  private final DefaultAttributeValueHandler rgbaHandler;
  private final DefaultAttributeValueHandler textureHandler;
  private final DefaultAttributeValueHandler customHandler;

  @AssistedInject
  private DefaultVertexBuilder(@Assisted VertexBufferObject vbo) {
    this.vbo = vbo;

    this.pos3fHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.POSITION3F);
    this.pos4fHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.POSITION4F);
    this.normalHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.NORMAL);
    this.rgbHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.COLOR_RGB);
    this.rgbaHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.COLOR_RGBA);
    this.textureHandler =
        new DefaultAttributeValueHandler(attribute -> attribute == VertexAttributes.TEXTURE_UV);
    this.customHandler =
        new DefaultAttributeValueHandler(attribute -> !(attribute instanceof EnumeratedVertexFormat));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder position(float x, float y, float z) {
    this.pos3fHandler.addFloats(x, y, z);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder position(float x, float y, float z, float w) {
    this.pos4fHandler.addFloats(x, y, z, w);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder normal(float x, float y, float z) {
    this.normalHandler.addFloats(x, y, z);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder color(float r, float g, float b) {
    this.rgbHandler.addFloats(r, g, b);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder color(byte r, byte g, byte b) {
    return this.color((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder color(byte r, byte g, byte b, byte a) {
    return this.color(
        (float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder color(float rgba) {
    int bits = Float.floatToIntBits(rgba);
    return this.color(
        (byte) (bits >> 24) & 0xff,
        (byte) (bits >> 16) & 0xff,
        (byte) (bits >> 8) & 0xff,
        (byte) (bits & 0xff));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder color(float r, float g, float b, float a) {
    this.rgbaHandler.addFloats(r, g, b, a);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder texture(float u, float v) {
    this.textureHandler.addFloats(u, v);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder custom(float... values) {
    this.customHandler.addFloats(values);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBuilder next() {
    return this.vbo.addVertex();
  }

  @Override
  public DefaultAttributeValueHandler getPos3fHandler() {
    return pos3fHandler;
  }

  @Override
  public DefaultAttributeValueHandler getPos4fHandler() {
    return pos4fHandler;
  }

  @Override
  public DefaultAttributeValueHandler getNormalHandler() {
    return normalHandler;
  }

  @Override
  public DefaultAttributeValueHandler getRgbHandler() {
    return rgbHandler;
  }

  @Override
  public DefaultAttributeValueHandler getRgbaHandler() {
    return rgbaHandler;
  }

  @Override
  public DefaultAttributeValueHandler getTextureHandler() {
    return textureHandler;
  }

  @Override
  public DefaultAttributeValueHandler getCustomHandler() {
    return customHandler;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int write(float[] buffer, int startOffset) {
    int offset = startOffset;
    for (VertexAttribute attribute : this.vbo.getFormat().getAttributes()) {
      if (attribute == VertexAttributes.POSITION3F) {
        this.pos3fHandler.writeFloats(buffer, offset);
      } else if (attribute == VertexAttributes.POSITION4F) {
        this.pos4fHandler.writeFloats(buffer, offset);
      } else if (attribute == VertexAttributes.NORMAL) {
        this.normalHandler.writeFloats(buffer, offset);
      } else if (attribute == VertexAttributes.COLOR_RGB) {
        this.rgbHandler.writeFloats(buffer, offset);
      } else if (attribute == VertexAttributes.COLOR_RGBA) {
        this.rgbaHandler.writeFloats(buffer, offset);
      } else if (attribute == VertexAttributes.TEXTURE_UV) {
        this.textureHandler.writeFloats(buffer, offset);
      } else if (!(attribute instanceof EnumeratedVertexFormat)) {
        this.customHandler.writeFloats(buffer, offset);
      } else {
        throw new IllegalStateException(
            "You're not supposed to implement EnumeratedVertexFormat yourself. Go away.");
      }
      offset += attribute.getSize();
    }
    return offset - startOffset;
  }

  private class DefaultAttributeValueHandler implements VertexBuilder.AttributeValueHandler{

    private final Function<VertexAttribute, Boolean> attributeTypeMatcher;
    private final Queue<Float> toWrite;
    private int pos;

    DefaultAttributeValueHandler(Function<VertexAttribute, Boolean> attributeTypeMatcher) {
      this.pos = 0;
      this.attributeTypeMatcher = attributeTypeMatcher;
      this.toWrite = new LinkedList<>();
    }

    @Override
    public void addFloats(float... floats) {
      int i = 0;
      for (VertexAttribute attribute :
          DefaultVertexBuilder.this.vbo.getFormat().getAttributes()) {
        if (this.attributeTypeMatcher.apply(attribute)) {
          if (i == this.pos) {
            if (floats.length != attribute.getSize()) {
              throw new IllegalArgumentException(
                  "The number of provided floats doesn't match the size of the attribute.");
            }
            for (float c : floats) {
              toWrite.add(c);
            }
            this.pos++;
            return;
          } else {
            i++;
          }
        }
      }

      throw new IllegalStateException(
          "Can't write (another) attribute of this type as the vertex format doesn't match.");
    }

    @Override
    public void writeFloats(float[] buffer, int offset) {
      if(this.pos == 0){
        throw new IllegalStateException(
            "Not enough attributes have been written to match the vertex format.");
      }
      int floatsPerAttribute = toWrite.size() / this.pos;
      for (int i = 0; i < floatsPerAttribute; i++) {
        if (this.toWrite.peek() != null) {
          buffer[offset + i] = this.toWrite.poll();
        } else {
          throw new IllegalStateException(
              "Not enough attributes have been written to match the vertex format.");
        }
      }
    }

    @Override
    public void clear() {
      this.toWrite.clear();
      this.pos = 0;
    }
  }
}
