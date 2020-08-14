package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public interface VertexBox {

  Vector3f getPosition();

  Vector3f getDimensions();

  float getHeight();

  float getDepth();

  float getWidth();

  float getPositionX();

  float getPositionY();

  float getPositionZ();

  short getLightMapU();

  short getLightMapV();

  int getLightMap();

  VertexBox setPosition(Vector3f position);

  VertexBox setPosition(float x, float y, float z);

  VertexBox setDimensions(Supplier<Vector3f> dimensions);

  VertexBox setDimensions(Vector3f dimensions);

  VertexBox setDimensions(float x, float y, float z);

  VertexBox setHeight(float height);

  VertexBox setDepth(float depth);

  VertexBox setWidth(float width);

  VertexBox setPositionX(float x);

  VertexBox setPositionY(float y);

  VertexBox setPositionZ(float z);

  VertexBox setLightMapV(short v);

  VertexBox setLightMapU(short u);

  VertexBox setLightMapMasked(int lightMap);

  VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  interface Builder {

    Builder withColor(Color color);

    Builder withColor(Supplier<Color> color);

    Builder withLightMap(int mask);

    Builder withLightMap(IntSupplier lightmap);

    Builder withTextureDensity(Supplier<Vector2f> textureDensity);

    Builder withTextureDensity(Vector2f textureDensity);

    Builder withTextureOffset(Supplier<Vector2f> textureOffset);

    Builder withTextureOffset(Vector2f textureOffset);

    VertexBox build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create(@Assisted("position") Vector3f position, @Assisted("dimensions") Vector3f dimensions);

      Builder create(@Assisted("position") Supplier<Vector3f> position, @Assisted("dimensions") Supplier<Vector3f> dimensions);
    }

  }


}
