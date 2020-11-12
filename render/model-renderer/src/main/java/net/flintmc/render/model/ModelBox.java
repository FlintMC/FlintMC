package net.flintmc.render.model;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ModelBox extends Renderable<ModelBox, Object> {

  float getRotationPointX();

  float getRotationPointY();

  float getRotationPointZ();

  float getRotationAngleX();

  float getRotationAngleY();

  float getRotationAngleZ();

  ModelBox setRotationPointX(float x);

  ModelBox setRotationPointY(float y);

  ModelBox setRotationPointZ(float z);

  ModelBox setRotationPoint(float x, float y, float z);

  ModelBox setRotationAngleX(float x);

  ModelBox setRotationAngleY(float y);

  ModelBox setRotationAngleZ(float z);

  ModelBox setRotationAngle(float x, float y, float z);

  @AssistedFactory(ModelBox.class)
  interface Factory {
    ModelBox create(@Assisted Supplier<RenderContext<?, ModelBox>> renderContext, @Assisted Object target);
  }
}
