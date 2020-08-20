package net.labyfy.component.render;

import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.awt.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public interface VertexQuad {

  Vector3f getVertex1();

  Vector3f getVertex2();

  Vector3f getVertex3();

  Vector3f getVertex4();

  Vector3f[] getVertices();

  Vector2f getVertex1TextureUV();

  Vector2f getVertex2TextureUV();

  Vector2f getVertex3TextureUV();

  Vector2f getVertex4TextureUV();

  Vector2f[] getVertexTextureUVs();

  Vector3i getNormal1();

  Vector3i getNormal2();

  Vector3i getNormal3();

  Vector3i getNormal4();

  Vector3i[] getNormals();

  Color getColor();

  int getLightMap();

  VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer);


  interface Builder {

    Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Vector3f> vertex4);

    Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4);

    Builder withNormals(Supplier<Vector3i> normal1, Supplier<Vector3i> normal2, Supplier<Vector3i> normal3, Supplier<Vector3i> normal4);

    Builder withNormals(Vector3i normal1, Vector3i normal2, Vector3i normal3, Vector3i normal4);

    Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV, Supplier<Vector2f> vertex4TextureUV);

    Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV, Vector2f vertex4TextureUV);

    Builder withColor(Supplier<Color> color);

    Builder withColor(Color color);

    Builder withLightMap(int lightMap);

    Builder withLightMap(IntSupplier lightMap);

    VertexQuad build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create();
    }

  }

}
