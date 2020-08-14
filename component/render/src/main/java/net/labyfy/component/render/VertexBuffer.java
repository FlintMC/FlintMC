package net.labyfy.component.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;

public interface VertexBuffer {

  VertexBuffer pos(float x, float y, float z);

  VertexBuffer color(int r, int g, int b, int alpha);

  VertexBuffer color(Color color);

  VertexBuffer pos(Vector3f position);

  VertexBuffer normal(float x, float y, float z);

  VertexBuffer normal(Vector3f normal);

  VertexBuffer end();

  VertexBuffer lightmap(short sky, short ground);

  VertexBuffer lightmap(int masked);

  VertexBuffer texture(float x, float y);

  VertexBuffer texture(Vector2f texture);

  int getVertexCount();

  VertexFormat getFormat();

  Matrix4f getWorldContext();

  VertexBuffer setWorldContext(Matrix4f matrix);

  Matrix3f getNormalContext();

  VertexBuffer setNormalContext(Matrix3f normalContext);

  AdvancedVertexBuffer advanced();

}
