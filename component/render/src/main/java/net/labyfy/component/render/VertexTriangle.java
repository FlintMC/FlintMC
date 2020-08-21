package net.labyfy.component.render;

import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public interface VertexTriangle {

  Vector3f getVertex1();

  Vector3f getVertex2();

  Vector3f getVertex3();

  Vector3f[] getVertices();

  Vector3f getNormal1();

  Vector3f getNormal2();

  Vector3f getNormal3();

  Vector3f[] getNormals();

  Vector2i getOverlay1();

  Vector2i getOverlay2();

  Vector2i getOverlay3();

  Vector2i[] getOverlays();

  Vector2f getVertex1TextureUV();

  Vector2f getVertex2TextureUV();

  Vector2f getVertex3TextureUV();

  Vector2f[] getVertexTextureUVs();

  int getLightMap();

  Color getColor();

  VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  interface Builder {

    Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3);

    Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3);

    Builder withOverlays(Vector2i overlay1, Vector2i overlay2, Vector2i overlay3);

    Builder withOverlays(Supplier<Vector2i> overlay1, Supplier<Vector2i> overlay2, Supplier<Vector2i> overlay3);

    Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV);

    Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV);

    Builder withColor(Supplier<Color> color);

    Builder withColor(Color color);

    Builder withLightMap(int lightMap);

    Builder withLightMap(IntSupplier lightMap);

    Builder withNormals(Supplier<Vector3f> normal1, Supplier<Vector3f> normal2, Supplier<Vector3f> normal3);

    Builder withNormals(Vector3f normal1, Vector3f normal2, Vector3f normal3);

    VertexTriangle build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create();
    }
  }

}
