package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class VertexTriangleImpl implements VertexTriangle {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3;

  private final Supplier<Color> color;

  private final Supplier<Vector2f>
      vertex1TextureUV,
      vertex2TextureUV,
      vertex3TextureUV;

  private final Supplier<Vector3f>
      normal1,
      normal2,
      normal3;

  private final Supplier<Vector2i>
      overlay1,
      overlay2,
      overlay3;

  private final IntSupplier lightMap;

  private VertexTriangleImpl(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Color> color, Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV, Supplier<Vector3f> normal1, Supplier<Vector3f> normal2, Supplier<Vector3f> normal3, Supplier<Vector2i> overlay1, Supplier<Vector2i> overlay2, Supplier<Vector2i> overlay3, IntSupplier lightMap) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
    this.color = color;
    this.vertex1TextureUV = vertex1TextureUV;
    this.vertex2TextureUV = vertex2TextureUV;
    this.vertex3TextureUV = vertex3TextureUV;
    this.normal1 = normal1;
    this.normal2 = normal2;
    this.normal3 = normal3;
    this.overlay1 = overlay1;
    this.overlay2 = overlay2;
    this.overlay3 = overlay3;
    this.lightMap = lightMap;
  }

  public Vector3f getVertex1() {
    return this.vertex1.get();
  }

  public Vector3f getVertex2() {
    return this.vertex2.get();
  }

  public Vector3f getVertex3() {
    return this.vertex3.get();
  }

  public Vector3f[] getVertices() {
    return new Vector3f[]{
        this.getVertex1(), this.getVertex2(), this.getVertex3()
    };
  }

  public Vector3f getNormal1() {
    return this.normal1.get();
  }

  public Vector3f getNormal2() {
    return this.normal2.get();
  }

  public Vector3f getNormal3() {
    return this.normal3.get();
  }

  public Vector3f[] getNormals() {
    return new Vector3f[]{this.getNormal1(), this.getNormal2(), this.getNormal3()};
  }

  public Vector2i getOverlay1() {
    return this.overlay1.get();
  }

  public Vector2i getOverlay2() {
    return this.overlay2.get();
  }

  public Vector2i getOverlay3() {
    return this.overlay3.get();
  }

  @Override
  public Vector2i[] getOverlays() {
    return new Vector2i[]{this.getOverlay1(), this.getOverlay2(), this.getOverlay3()};
  }

  public Vector2f getVertex1TextureUV() {
    return vertex1TextureUV.get();
  }

  public Vector2f getVertex2TextureUV() {
    return vertex2TextureUV.get();
  }

  public Vector2f getVertex3TextureUV() {
    return vertex3TextureUV.get();
  }

  public Vector2f[] getVertexTextureUVs() {
    return new Vector2f[]{this.getVertex1TextureUV(), this.getVertex2TextureUV(), this.getVertex3TextureUV()};
  }

  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  public Color getColor() {
    return this.color.get();
  }

  public VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    matrixStack
        .push()
        .scale(1, -1, -1);
    vertexBuffer
        .pos(this.getVertex1())
        .normal(this.getNormal1())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex1TextureUV())
        .overlay(this.getOverlay1())
        .end()
        .pos(this.getVertex2())
        .normal(this.getNormal2())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex2TextureUV())
        .overlay(this.getOverlay2())
        .end()
        .pos(this.getVertex3())
        .normal(this.getNormal3())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex3TextureUV())
        .overlay(this.getOverlay3())
        .end();
    matrixStack.pop();
    return this;
  }

  @Implement(Builder.class)
  public static class BuilderImpl implements Builder {

    private Supplier<Vector3f>
        vertex1,
        vertex2,
        vertex3;

    private Supplier<Color> color = () -> null;

    private IntSupplier lightMap = () -> 0;

    private Supplier<Vector2f>
        vertex1TextureUV = () -> null,
        vertex2TextureUV = () -> null,
        vertex3TextureUV = () -> null;

    private Supplier<Vector3f>
        normal1 = () -> null,
        normal2 = () -> null,
        normal3 = () -> null;

    private Supplier<Vector2i>
        overlay1 = () -> null,
        overlay2 = () -> null,
        overlay3 = () -> null;


    @AssistedInject
    private BuilderImpl() {
    }


    public Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.vertex3 = vertex3;
      return this;
    }

    public Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV) {
      this.vertex1TextureUV = vertex1TextureUV;
      this.vertex2TextureUV = vertex2TextureUV;
      this.vertex3TextureUV = vertex3TextureUV;
      return this;
    }


    public Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV) {
      return this.withTextureUVs(() -> new Vector2f(vertex1TextureUV), () -> new Vector2f(vertex2TextureUV), () -> new Vector2f(vertex3TextureUV));
    }

    public Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
      return this.withVertices(() -> new Vector3f(vertex1), () -> new Vector3f(vertex2), () -> new Vector3f(vertex3));
    }

    public Builder withOverlays(Vector2i overlay1, Vector2i overlay2, Vector2i overlay3) {
      return this.withOverlays(() -> new Vector2i(overlay1), () -> new Vector2i(overlay2), () -> new Vector2i(overlay3));
    }

    public Builder withOverlays(Supplier<Vector2i> overlay1, Supplier<Vector2i> overlay2, Supplier<Vector2i> overlay3) {
      this.overlay1 = overlay1;
      this.overlay2 = overlay2;
      this.overlay3 = overlay3;
      return this;
    }

    public Builder withColor(Supplier<Color> color) {
      this.color = color;
      return this;
    }

    public Builder withColor(Color color) {
      return this.withColor(() -> color);
    }

    public Builder withLightMap(int lightMap) {
      return this.withLightMap(() -> lightMap);
    }

    public Builder withLightMap(IntSupplier lightMap) {
      this.lightMap = lightMap;
      return this;
    }

    public Builder withNormals(Supplier<Vector3f> normal1, Supplier<Vector3f> normal2, Supplier<Vector3f> normal3) {
      this.normal1 = normal1;
      this.normal2 = normal2;
      this.normal3 = normal3;
      return this;
    }

    public Builder withNormals(Vector3f normal1, Vector3f normal2, Vector3f normal3) {
      return this.withNormals(() -> new Vector3f(normal1), () -> new Vector3f(normal2), () -> new Vector3f(normal3));
    }

    public VertexTriangle build() {
      Objects.requireNonNull(this.vertex1);
      Objects.requireNonNull(this.vertex2);
      Objects.requireNonNull(this.vertex3);
      return new VertexTriangleImpl(this.vertex1, this.vertex2, this.vertex3, this.color, this.vertex1TextureUV, this.vertex2TextureUV, this.vertex3TextureUV, normal1, normal2, normal3, overlay1, overlay2, overlay3, this.lightMap);
    }
  }

}
