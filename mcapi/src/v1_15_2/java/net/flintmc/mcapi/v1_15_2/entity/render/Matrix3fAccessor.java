package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.client.renderer.Matrix3f")
public interface Matrix3fAccessor {
  @FieldGetter("m00")
  float getM00();

  @FieldGetter("m01")
  float getM01();

  @FieldGetter("m02")
  float getM02();

  @FieldGetter("m10")
  float getM10();

  @FieldGetter("m11")
  float getM11();

  @FieldGetter("m12")
  float getM12();

  @FieldGetter("m20")
  float getM20();

  @FieldGetter("m21")
  float getM21();

  @FieldGetter("m22")
  float getM22();
}
