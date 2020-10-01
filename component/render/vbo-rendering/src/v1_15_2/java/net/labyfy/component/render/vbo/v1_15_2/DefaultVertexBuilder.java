package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.*;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

@Implement(VertexBuilder.class)
public class DefaultVertexBuilder implements VertexBuilder {

  private final VertexBufferObject vbo;

  private final AttributeValueHandler pos3fHandler;
  private final AttributeValueHandler pos4fHandler;
  private final AttributeValueHandler normalHandler;
  private final AttributeValueHandler rgbHandler;
  private final AttributeValueHandler rgbaHandler;
  private final AttributeValueHandler textureHandler;
  private final AttributeValueHandler customHandler;

  @AssistedInject
  private DefaultVertexBuilder(@Assisted VertexBufferObject vbo) {
    this.vbo = vbo;

    this.pos3fHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.POSITION3F);
    this.pos4fHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.POSITION4F);
    this.normalHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.NORMAL);
    this.rgbHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.COLOR_RGB);
    this.rgbaHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.COLOR_RGBA);
    this.textureHandler =
        new AttributeValueHandler(attribute -> attribute == VertexAttributes.TEXTURE_UV);
    this.customHandler =
        new AttributeValueHandler(attribute -> !(attribute instanceof EnumeratedVertexFormat));
  }

  @Override
  public VertexBuilder position(float x, float y, float z) {
    this.pos3fHandler.addFloats(x, y, z);
    return this;
  }

  @Override
  public VertexBuilder position(float x, float y, float z, float w) {
    this.pos4fHandler.addFloats(x, y, z, w);
    return this;
  }

  @Override
  public VertexBuilder normal(float x, float y, float z) {
    this.normalHandler.addFloats(x, y, z);
    return this;
  }

  @Override
  public VertexBuilder color(float r, float g, float b) {
    this.rgbHandler.addFloats(r, g, b);
    return this;
  }

  @Override
  public VertexBuilder color(byte r, byte g, byte b) {
    return this.color((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
  }

  @Override
  public VertexBuilder color(byte r, byte g, byte b, byte a) {
    return this.color(
        (float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
  }

  @Override
  public VertexBuilder color(float rgba) {
    int bits = Float.floatToIntBits(rgba);
    return this.color(
        (byte) (bits >> 24) & 0xff,
        (byte) (bits >> 16) & 0xff,
        (byte) (bits >> 8) & 0xff,
        (byte) (bits & 0xff));
  }

  @Override
  public VertexBuilder color(float r, float g, float b, float a) {
    this.rgbaHandler.addFloats(r, g, b, a);
    return this;
  }

  @Override
  public VertexBuilder texture(float u, float v) {
    this.textureHandler.addFloats(u, v);
    return this;
  }

  @Override
  public VertexBuilder custom(float... values) {
    this.customHandler.addFloats(values);
    return this;
  }

  @Override
  public VertexBuilder next() {
    return this.vbo.addVertex();
  }

  @Override
  public void write(FloatBuffer buffer) {
    this.vbo
        .getFormat()
        .getAttributes()
        .forEach(
            attribute -> {
              if (attribute == VertexAttributes.POSITION3F) this.pos3fHandler.writeFloats(buffer);
              else if (attribute == VertexAttributes.POSITION4F)
                this.pos4fHandler.writeFloats(buffer);
              else if (attribute == VertexAttributes.NORMAL) this.normalHandler.writeFloats(buffer);
              else if (attribute == VertexAttributes.COLOR_RGB) this.rgbHandler.writeFloats(buffer);
              else if (attribute == VertexAttributes.COLOR_RGBA)
                this.rgbaHandler.writeFloats(buffer);
              else if (attribute == VertexAttributes.TEXTURE_UV)
                this.textureHandler.writeFloats(buffer);
              else if (!(attribute instanceof EnumeratedVertexFormat))
                this.customHandler.writeFloats(buffer);
              else
                throw new IllegalStateException(
                    "You're not supposed to implement EnumeratedVertexFormat yourself. Go away.");
            });
  }

  private class AttributeValueHandler {

    private int pos;
    private final Function<VertexAttribute, Boolean> attributeTypeMatcher;
    private final Queue<Float> toWrite;

    AttributeValueHandler(Function<VertexAttribute, Boolean> attributeTypeMatcher) {
      this.pos = 0;
      this.attributeTypeMatcher = attributeTypeMatcher;
      this.toWrite = new LinkedList<>();
    }

    void addFloats(float... floats) {
      int i = 0;
      for (VertexAttribute attribute : DefaultVertexBuilder.this.vbo.getFormat().getAttributes()) {
        if (this.attributeTypeMatcher.apply(attribute) && i == this.pos) {
          if (floats.length != attribute.getSize())
            throw new IllegalArgumentException(
                "The number of provided floats doesn't match the size of the attribute.");
          for (float c : floats) toWrite.add(c);
          this.pos++;
          return;
        } else i++;
      }

      throw new IllegalStateException(
          "Can't write (another) attribute of this type as the vertex format doesn't match.");
    }

    void writeFloats(FloatBuffer buffer) {
      int floatsPerAttribute = toWrite.size() / this.pos;
      for (int i = 0; i < floatsPerAttribute; i++) {
        if (this.toWrite.peek() != null) buffer.put(this.toWrite.poll());
        else
          throw new IllegalStateException(
              "Not enough attributes have been written to match the vertex format.");
      }
    }
  }
}
