package net.labyfy.component.commons.math;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.function.Supplier;

@Singleton
public class MathFactory {

  private final GenericMathFactory<Vector3f> vec3fs = new GenericMathFactory<>(Vector3f::new);
  private final GenericMathFactory<Matrix4f> matrix4fs = new GenericMathFactory<>(Matrix4f::new);
  private final GenericMathFactory<Matrix3f> matrix3fs = new GenericMathFactory<>(Matrix3f::new);
  private final GenericMathFactory<Quaternionf> quaternionfs = new GenericMathFactory<>(Quaternionf::new);

  public Matrix3f getMatrix3f() {
    return this.matrix3fs.get().identity();
  }

  public Matrix3f getMatrix3f(Matrix3f matrix3f) {
    return this.matrix3fs.get().set(matrix3f);
  }

  public Matrix3f getMatrix3f(float m00, float m01, float m02,
                              float m10, float m11, float m12,
                              float m20, float m21, float m22) {
    return this.matrix3fs.get().set(m00, m01, m02, m10, m11, m12, m20, m21, m22);
  }

  public Matrix4f getMatrix4f() {
    return this.matrix4fs.get().identity();
  }

  public Matrix4f getMatrix4f(Matrix4f matrix4f) {
    return this.matrix4fs.get().set(matrix4f);
  }

  public Matrix4f getMatrix4f(float m00, float m01, float m02, float m03,
                              float m10, float m11, float m12, float m13,
                              float m20, float m21, float m22, float m23,
                              float m30, float m31, float m32, float m33) {
    return this.matrix4fs.get().set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }

  public Quaternionf getQuaternionf(Quaternionf quaternionf) {
    return this.quaternionfs.get().set(quaternionf);
  }

  public Quaternionf getQuaternionf(float x, float y, float z, float w) {
    return this.quaternionfs.get().set(x, y, z, w);
  }

  public Quaternionf getQuaternionf() {
    return this.quaternionfs.get().set(0, 0, 0, 1);
  }

  public Vector3f getVector3f() {
    return getVector3f(0, 0, 0);
  }

  public Vector3f getVector3f(float x, float y, float z) {
    return this.vec3fs.get().set(x, y, z);
  }

  public void reset() {
    this.vec3fs.vecPointer = 0;
    this.matrix4fs.vecPointer = 0;
    this.matrix3fs.vecPointer = 0;
    this.quaternionfs.vecPointer = 0;
  }

  private class GenericMathFactory<T> {

    private final Supplier<T> allocator;
    private ArrayList<T> elements = new ArrayList<>();
    private int vecPointer;

    private GenericMathFactory(Supplier<T> allocator) {
      this.allocator = allocator;
      this.vecPointer = 0;
    }

    public T get() {
      if (vecPointer == elements.size()) {
        elements.add(allocator.get());
      }
      T vec = elements.get(vecPointer);
      vecPointer++;
      return vec;
    }

  }
}
