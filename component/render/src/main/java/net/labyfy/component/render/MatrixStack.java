package net.labyfy.component.render;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * This class represents an opengl transformation context with the most important utility functions to modify the world matrix.
 * This is necessary because Minecraft starts to use batched rendering in the 1.15.2 and in order to maintain a cross version
 * rendering module, batched rendering had to be implemented.
 * <p>
 * See also at {@link VertexBuffer}.
 */
public interface MatrixStack {

  /**
   * Similar to glPush().
   *
   * @return this
   */
  MatrixStack push();

  /**
   * Similar to glPop().
   *
   * @return this
   */
  MatrixStack pop();

  /**
   * Rotates the world matrix with a quaternion.
   *
   * @param quaternionf the rotation to rotate the world matrix with.
   * @return this
   */
  MatrixStack rotate(Quaternionf quaternionf);

  /**
   * Rotates the world matrix around a given axis.
   *
   * @param angle the angle in radians to rotate the matrix with
   * @param xAxis the x component of the axis to rotate the world matrix around with
   * @param yAxis the y component of the axis to rotate the world matrix around with
   * @param zAxis the z component of the axis to rotate the world matrix around with
   * @return this
   */
  MatrixStack rotate(float angle, float xAxis, float yAxis, float zAxis);


  /**
   * Rotates the world matrix around a given axis.
   *
   * @param angle the angle in radians to rotate the matrix with
   * @param axis  the axis to rotate the world matrix around with
   * @return this
   */
  MatrixStack rotate(float angle, Vector3f axis);

  /**
   * Rotates the world matrix around a given axis.
   *
   * @param angle  the angle in radians to rotate the matrix with
   * @param xAxis  the x component of the axis to rotate the world matrix around with
   * @param yAxis  the y component of the axis to rotate the world matrix around with
   * @param zAxis  the z component of the axis to rotate the world matrix around with
   * @param xPoint the x component of the point to rotate the world matrix around with
   * @param yPoint the y component of the point to rotate the world matrix around with
   * @param zPoint the z component of the point to rotate the world matrix around with
   * @return this
   */
  MatrixStack rotate(float angle, float xAxis, float yAxis, float zAxis, float xPoint, float yPoint, float zPoint);

  /**
   * Rotates the world matrix around a given axis.
   *
   * @param angle the angle in radians to rotate the matrix with
   * @param axis  the axis to rotate the world matrix around with
   * @param point the point to rotate the world matrix around with
   * @return this
   */
  MatrixStack rotate(float angle, Vector3f axis, Vector3f point);

  /**
   * Rotates the world matrix with a given rotation around a point.
   *
   * @param rotation the rotation to rotate the world matrix with
   * @param point    the point to rotate the world matrix around with
   * @return this
   */
  MatrixStack rotate(Quaternionf rotation, Vector3f point);

  /**
   * Similar to glTranslatef(FFF).
   *
   * @param vector3f the vector to translate the world matrix with
   * @return this
   */
  MatrixStack translate(Vector3f vector3f);

  /**
   * Similar to glTranslatef(FFF).
   *
   * @param x the x component of the vector to translate the world matrix with
   * @param y the y component of the vector to translate the world matrix with
   * @param z the z component of the vector to translate the world matrix with
   * @return this
   */
  MatrixStack translate(float x, float y, float z);

  /**
   * Similar to glScalef(FFF).
   *
   * @param x the x component of the scale factor
   * @param y the y component of the scale factor
   * @param z the z component of the scale factor
   * @return this
   */
  MatrixStack scale(float x, float y, float z);

  /**
   * Similar to glScalef(FFF).
   *
   * @param scale the scale factor
   * @return this
   */
  MatrixStack scale(float scale);


  /**
   * Similar to glScalef(FFF).
   *
   * @param scale the scale factor
   * @return this
   */
  MatrixStack scale(Vector3f scale);

}
