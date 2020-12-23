package net.flintmc.util.math.internal.rotation;

import net.flintmc.util.math.rotation.Quaternionf;
import org.joml.Math;

public class DefaultQuaternionf implements Quaternionf {

  private float x;
  private float y;
  private float z;
  private float w;

  @Override
  public Float getX() {
    return this.x;
  }

  @Override
  public Float getY() {
    return this.y;
  }

  @Override
  public Float getZ() {
    return this.z;
  }

  @Override
  public Float getW() {
    return this.w;
  }

  @Override
  public Quaternionf setX(Float x) {
    this.x = x;
    return this;
  }

  @Override
  public Quaternionf setY(Float y) {
    this.y = y;
    return this;
  }

  @Override
  public Quaternionf setZ(Float z) {
    this.z = z;
    return this;
  }

  @Override
  public Quaternionf setW(Float w) {
    this.w = w;
    return this;
  }

  @Override
  public Quaternionf rotateLocalX(Float ang) {
    return this.rotateLocalX(ang, this);
  }

  @Override
  public Quaternionf rotateLocalX(Float angle, Quaternionf target) {
    float hangle = angle * 0.5f;
    float s = Math.sin(hangle);
    float c = Math.cosFromSin(s, hangle);
    target.set(c * x + s * w, c * y - s * z, c * z + s * y, c * w - s * x);
    return target;
  }

  @Override
  public Quaternionf rotateLocalY(Float ang) {
    return this.rotateLocalY(ang, this);
  }

  @Override
  public Quaternionf rotateLocalY(Float angle, Quaternionf target) {
    float hangle = angle * 0.5f;
    float s = Math.sin(hangle);
    float c = Math.cosFromSin(s, hangle);
    target.set(c * x + s * z, c * y + s * w, c * z - s * x, c * w - s * y);
    return target;
  }

  @Override
  public Quaternionf rotateLocalZ(Float ang) {
    return this.rotateLocalZ(ang, this);
  }

  @Override
  public Quaternionf rotateLocalZ(Float angle, Quaternionf target) {
    float hangle = angle * 0.5f;
    float s = Math.sin(hangle);
    float c = Math.cosFromSin(s, hangle);
    target.set(c * x - s * y, c * y + s * x, c * z + s * w, c * w - s * z);
    return target;
  }

  @Override
  public Quaternionf set(Float x, Float y, Float z, Float w) {
    return this.setX(x).setY(y).setZ(z).setW(w);
  }
}
