package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexQuad;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class VertexQuadImpl implements VertexQuad {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3,
      vertex4;

  private final Supplier<Color> color;

  private final VertexTriangle
      triangle1,
      triangle2;

  private final Supplier<Vector2f>
      vertex1TextureUV,
      vertex2TextureUV,
      vertex3TextureUV,
      vertex4TextureUV;

  private final Supplier<Vector3f>
      normal1,
      normal2,
      normal3,
      normal4;

  private final Supplier<Vector2i>
      overlay1,
      overlay2,
      overlay3,
      overlay4;

  private final IntSupplier lightMap;

  private VertexQuadImpl(
      VertexTriangle.Builder.Factory vertexTriangleBuilderFactory,
      Supplier<Vector3f> vertex1,
      Supplier<Vector3f> vertex2,
      Supplier<Vector3f> vertex3,
      Supplier<Vector3f> vertex4,
      Supplier<Color> color,
      Supplier<Vector2f> vertex1TextureUV,
      Supplier<Vector2f> vertex2TextureUV,
      Supplier<Vector2f> vertex3TextureUV,
      Supplier<Vector2f> vertex4TextureUV,
      Supplier<Vector3f> normal1,
      Supplier<Vector3f> normal2,
      Supplier<Vector3f> normal3,
      Supplier<Vector3f> normal4,
      Supplier<Vector2i> overlay1,
      Supplier<Vector2i> overlay2,
      Supplier<Vector2i> overlay3,
      Supplier<Vector2i> overlay4,
      IntSupplier lightMap) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
    this.vertex4 = vertex4;
    this.color = color;
    this.vertex1TextureUV = vertex1TextureUV;
    this.vertex2TextureUV = vertex2TextureUV;
    this.vertex3TextureUV = vertex3TextureUV;
    this.vertex4TextureUV = vertex4TextureUV;
    this.normal1 = normal1;
    this.normal2 = normal2;
    this.normal3 = normal3;
    this.normal4 = normal4;
    this.overlay1 = overlay1;
    this.overlay2 = overlay2;
    this.overlay3 = overlay3;
    this.overlay4 = overlay4;
    this.lightMap = lightMap;

    this.triangle1 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex2,
            this::getVertex3
        )
        .withColor(this::getColor)
        .withLightMap(this::getLightMap)
        .withTextureUVs(
            this::getVertex1TextureUV,
            this::getVertex2TextureUV,
            this::getVertex3TextureUV
        )
        .withOverlays(
            this::getOverlay1,
            this::getOverlay2,
            this::getOverlay3
        )
        .withNormals(
            this::getNormal1,
            this::getNormal2,
            this::getNormal3
        )
        .build();

    this.triangle2 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex3,
            this::getVertex4
        )
        .withColor(this::getColor)
        .withLightMap(this::getLightMap)
        .withTextureUVs(
            this::getVertex1TextureUV,
            this::getVertex3TextureUV,
            this::getVertex4TextureUV
        )
        .withOverlays(
            this::getOverlay1,
            this::getOverlay3,
            this::getOverlay4
        )
        .withNormals(
            this::getNormal1,
            this::getNormal3,
            this::getNormal4
        )
        .build();

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

  public Vector3f getVertex4() {
    return this.vertex4.get();
  }

  public Vector3f[] getVertices() {
    return new Vector3f[]{this.getVertex1(), this.getVertex2(), this.getVertex3(), this.getVertex4()};
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

  public Vector2f getVertex4TextureUV() {
    return vertex4TextureUV.get();
  }

  public Vector2f[] getVertexTextureUVs() {
    return new Vector2f[]{this.getVertex1TextureUV(), this.getVertex2TextureUV(), this.getVertex3TextureUV(), this.getVertex4TextureUV()};
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

  public Vector3f getNormal4() {
    return this.normal4.get();
  }

  public Vector3f[] getNormals() {
    return new Vector3f[]{this.getNormal1(), this.getNormal2(), getNormal3(), getNormal4()};
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

  public Vector2i getOverlay4() {
    return this.overlay4.get();
  }

  public Vector2i[] getOverlays() {
    return new Vector2i[]{this.getOverlay1(), this.getOverlay2(), this.getOverlay3(), this.getOverlay4()};
  }

  public Color getColor() {
    return this.color.get();
  }

  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  public VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.triangle1.render(matrixStack, vertexBuffer);
    this.triangle2.render(matrixStack, vertexBuffer);
    return this;
  }

  @Implement(VertexQuad.Builder.class)
  public static class BuilderImpl implements VertexQuad.Builder {

    private final VertexTriangle.Builder.Factory vertexTriangleBuilderFactory;
    private Supplier<Vector3f>
        vertex1,
        vertex2,
        vertex3,
        vertex4;

    private Supplier<Color> color = () -> null;

    private Supplier<Vector2f>
        vertex1TextureUV = () -> null,
        vertex2TextureUV = () -> null,
        vertex3TextureUV = () -> null,
        vertex4TextureUV = () -> null;

    private Supplier<Vector3f>
        normal1 = () -> null,
        normal2 = () -> null,
        normal3 = () -> null,
        normal4 = () -> null;

    private Supplier<Vector2i>
        overlay1 = () -> null,
        overlay2 = () -> null,
        overlay3 = () -> null,
        overlay4 = () -> null;

    private IntSupplier lightMap = () -> 0;

    @AssistedInject
    private BuilderImpl(VertexTriangle.Builder.Factory vertexTriangleBuilderFactory) {
      this.vertexTriangleBuilderFactory = vertexTriangleBuilderFactory;
    }

    public Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Vector3f> vertex4) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.vertex3 = vertex3;
      this.vertex4 = vertex4;
      return this;
    }

    public Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4) {
      return this.withVertices(() -> vertex1, () -> vertex2, () -> vertex3, () -> vertex4);
    }

    public Builder withNormals(Supplier<Vector3f> normal1, Supplier<Vector3f> normal2, Supplier<Vector3f> normal3, Supplier<Vector3f> normal4) {
      this.normal1 = normal1;
      this.normal2 = normal2;
      this.normal3 = normal3;
      this.normal4 = normal4;
      return this;
    }

    public Builder withNormals(Vector3f normal1, Vector3f normal2, Vector3f normal3, Vector3f normal4) {
      return this.withNormals(() -> new Vector3f(normal1), () -> new Vector3f(normal2), () -> new Vector3f(normal3), () -> new Vector3f(normal4));
    }

    public Builder withOverlays(Supplier<Vector2i> overlay1, Supplier<Vector2i> overlay2, Supplier<Vector2i> overlay3, Supplier<Vector2i> overlay4) {
      this.overlay1 = overlay1;
      this.overlay2 = overlay2;
      this.overlay3 = overlay3;
      this.overlay4 = overlay4;
      return this;
    }

    public Builder withOverlays(Vector2i overlay1, Vector2i overlay2, Vector2i overlay3, Vector2i overlay4) {
      return this.withOverlays(() -> new Vector2i(overlay1), () -> new Vector2i(overlay2), () -> new Vector2i(overlay3), () -> new Vector2i(overlay4));
    }

    public Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV, Supplier<Vector2f> vertex4TextureUV) {
      this.vertex1TextureUV = vertex1TextureUV;
      this.vertex2TextureUV = vertex2TextureUV;
      this.vertex3TextureUV = vertex3TextureUV;
      this.vertex4TextureUV = vertex4TextureUV;
      return this;
    }


    public Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV, Vector2f vertex4TextureUV) {
      return this.withTextureUVs(() -> vertex1TextureUV, () -> vertex2TextureUV, () -> vertex3TextureUV, () -> vertex4TextureUV);
    }

    public Builder withColor(Supplier<Color> color) {
      this.color = color;
      return this;
    }

    public Builder withColor(Color color) {
      return this.withColor(() -> color);
    }

    public VertexQuad.Builder withLightMap(int lightMap) {
      return this.withLightMap(() -> lightMap);
    }

    public VertexQuad.Builder withLightMap(IntSupplier lightMap) {
      this.lightMap = lightMap;
      return this;
    }

    public VertexQuad build() {
      Objects.requireNonNull(vertex1);
      Objects.requireNonNull(vertex2);
      Objects.requireNonNull(vertex3);
      Objects.requireNonNull(vertex4);
      return new VertexQuadImpl(
          vertexTriangleBuilderFactory,
          vertex1, vertex2, vertex3, vertex4,
          color,
          vertex1TextureUV, vertex2TextureUV, vertex3TextureUV, vertex4TextureUV,
          normal1, normal2, normal3, normal4,
          overlay1, overlay2, overlay3, overlay4,
          lightMap);
    }
  }

}
