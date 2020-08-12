package net.labyfy.component.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface VertexBuffer {

  VertexBuffer pos(float x, float y, float z);

  VertexBuffer color(int r, int g, int b, int alpha);

  VertexBuffer pos(Vector3f position);

  VertexBuffer normal(float x, float y, float z);

  VertexBuffer normal(Vector3f normal);

  VertexBuffer end();

  VertexBuffer lightmap(short sky, short ground);

  VertexBuffer texture(float x, float y);

  int getVertexCount();

  VertexFormat getFormat();

  VertexBuffer box(float x, float y, float z, float width, float height, float depth);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, short lightmapGround, short lightmapSky);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, int r, int g, int b, int alpha);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, int r, int g, int b, int alpha, short lightmapGround, short lightmapSky);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, short lightmapGround, short lightmapSky);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, int r, int g, int b, int alpha);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, int r, int g, int b, int alpha, short lightmapGround, short lightmapSky);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, float textureOffsetX, float textureOffsetY);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, float textureOffsetX, float textureOffsetY, short lightmapGround, short lightmapSky);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, float textureOffsetX, float textureOffsetY, int r, int g, int b, int alpha);

  VertexBuffer box(float x, float y, float z, float width, float height, float depth, float textureDensityX, float textureDensityY, float textureOffsetX, float textureOffsetY, int r, int g, int b, int alpha, short lightmapGround, short lightmapSky);

  VertexBuffer quad(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4
  );

  VertexBuffer quad(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer quad(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4,
      int r, int g, int b, int alpha
  );

  VertexBuffer quad(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4,
      int r, int g, int b, int alpha,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer quad(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      float x4, float y4, float z4, float texU4, float texV4
  );

  VertexBuffer quad(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      float x4, float y4, float z4, float texU4, float texV4,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer quad(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      float x4, float y4, float z4, float texU4, float texV4,
      int r, int g, int b, int alpha
  );

  VertexBuffer quad(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      float x4, float y4, float z4, float texU4, float texV4,
      int r, int g, int b, int alpha,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer triangle(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3
  );

  VertexBuffer triangle(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer triangle(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3
  );

  VertexBuffer triangle(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer triangle(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      int r, int g, int b, int alpha
  );

  VertexBuffer triangle(
      float x1, float y1, float z1, float texU1, float texV1,
      float x2, float y2, float z2, float texU2, float texV2,
      float x3, float y3, float z3, float texU3, float texV3,
      int r, int g, int b, int alpha,
      short lightmapGround, short lightmapSky
  );

  VertexBuffer triangle(
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      int r, int g, int b, int alpha
  );

  Matrix4f getWorldContext();

  VertexBuffer setWorldContext(Matrix4f matrix);

  Matrix3f getNormalContext();

  VertexBuffer setNormalContext(Matrix3f normalContext);

  AdvancedVertexBuffer advanced();

}
