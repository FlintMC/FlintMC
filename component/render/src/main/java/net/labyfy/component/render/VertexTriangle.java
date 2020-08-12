package net.labyfy.component.render;

import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector3f;

import java.util.function.Supplier;

public interface VertexTriangle {

  Vector3f getVertex1();

  Vector3f getVertex2();

  Vector3f getVertex3();

  Vector3f[] getVertices();

  VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  interface Builder {

    Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3);

    VertexTriangle build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create();
    }
  }

}
