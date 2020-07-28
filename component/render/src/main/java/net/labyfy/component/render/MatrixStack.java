package net.labyfy.component.render;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface MatrixStack {

  MatrixStack push();

  MatrixStack pop();

  MatrixStack rotate(Quaternionf quaternionf);

  MatrixStack rotate(float x, float y, float z, float w);

  MatrixStack translate(Vector3f vector3f);

  MatrixStack translate(float x, float y, float z);

  boolean clear();

}
