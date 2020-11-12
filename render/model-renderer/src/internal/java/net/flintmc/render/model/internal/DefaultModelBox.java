package net.flintmc.render.model.internal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderableRepository;

import java.util.function.Supplier;

@Implement(ModelBox.class)
public class DefaultModelBox extends DefaultRenderable<ModelBox, Object> implements ModelBox {

  private float rotationAngleX;
  private float rotationAngleY;
  private float rotationAngleZ;

  private float rotationPointX;
  private float rotationPointY;
  private float rotationPointZ;

  @AssistedInject
  private DefaultModelBox(@Assisted Supplier<RenderContext<?, ModelBox>> renderContext, @Assisted Object target, RenderableRepository repository) {
    super(renderContext, repository, target);
  }

  public float getRotationAngleX() {
    return rotationAngleX;
  }

  public DefaultModelBox setRotationAngleX(float rotationAngleX) {
    this.rotationAngleX = rotationAngleX;
    return this;
  }

  public float getRotationAngleY() {
    return rotationAngleY;
  }

  public DefaultModelBox setRotationAngleY(float rotationAngleY) {
    this.rotationAngleY = rotationAngleY;
    return this;
  }

  public float getRotationAngleZ() {
    return rotationAngleZ;
  }

  public DefaultModelBox setRotationAngleZ(float rotationAngleZ) {
    this.rotationAngleZ = rotationAngleZ;
    return this;
  }

  public float getRotationPointX() {
    return rotationPointX;
  }

  public DefaultModelBox setRotationPointX(float rotationPointX) {
    this.rotationPointX = rotationPointX;
    return this;
  }

  public float getRotationPointY() {
    return rotationPointY;
  }

  public DefaultModelBox setRotationPointY(float rotationPointY) {
    this.rotationPointY = rotationPointY;
    return this;
  }

  public float getRotationPointZ() {
    return rotationPointZ;
  }

  public DefaultModelBox setRotationPointZ(float rotationPointZ) {
    this.rotationPointZ = rotationPointZ;
    return this;
  }

  public DefaultModelBox setRotationAngle(float x, float y, float z) {
    this.setRotationAngleX(x);
    this.setRotationAngleY(y);
    this.setRotationAngleZ(z);
    return this;
  }

  public DefaultModelBox setRotationPoint(float x, float y, float z) {
    this.setRotationPointX(x);
    this.setRotationPointY(y);
    this.setRotationPointZ(z);
    return this;
  }


}
