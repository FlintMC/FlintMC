package net.labyfy.component.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface VertexBuffer {

  VertexBuffer pos(float x, float y, float z);

  VertexBuffer pos(Vector3f position);

  VertexBuffer normal(float x, float y, float z);

  VertexBuffer normal(Vector3f normal);

  VertexBuffer end();

  int getVertexCount();

  VertexFormat getFormat();

  VertexBuffer box(float x, float y, float z, float width, float height, float depth);

  Matrix4f getWorldContext();

  VertexBuffer setWorldContext(Matrix4f matrix);

  Matrix3f getNormalContext();

  VertexBuffer setNormalContext(Matrix3f normalContext);

  AdvancedVertexBuffer advanced();
}
