package net.labyfy.component.render;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface MatrixStack {

  MatrixStack push();

  MatrixStack pop();

  MatrixStack rotate(Quaternionf quaternionf);

  MatrixStack rotate(float angle, float xAxis, float yAxis, float zAxis);

  MatrixStack rotate(float angle, Vector3f axis);

  MatrixStack rotate(float angle, float xAxis, float yAxis, float zAxis, float xPoint, float yPoint, float zPoint);

  MatrixStack rotate(float angle, Vector3f axis, Vector3f point);

  MatrixStack rotate(Quaternionf rotation, Vector3f point);

  MatrixStack translate(Vector3f vector3f);

  MatrixStack translate(float x, float y, float z);

  MatrixStack scale(float x, float y, float z);

  MatrixStack scale(float scale);

  MatrixStack scale(Vector3f vector3f);

  boolean clear();

}
