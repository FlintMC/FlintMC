package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector3f;

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

  int getLightMapMasked();

  VertexBox setPosition(Vector3f position);

  VertexBox setPosition(float x, float y, float z);

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

  @AssistedFactory(VertexBox.class)
  interface Factory{
    VertexBox create(@Assisted("position") Vector3f position, @Assisted("dimensions") Vector3f dimensions);
  }

}
