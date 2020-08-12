package net.labyfy.component.render;

import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector3f;

import java.util.function.Supplier;

public interface VertexQuad {


  Vector3f getVertex1();

  Vector3f getVertex2();


  Vector3f getVertex3();

  Vector3f getVertex4();

  Vector3f[] getVertices();

  VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  interface Builder {

    Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Vector3f> vertex4);

    VertexQuad build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create();
    }

  }

}
