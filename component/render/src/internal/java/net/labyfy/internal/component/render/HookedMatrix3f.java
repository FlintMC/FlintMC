package net.labyfy.internal.component.render;

public interface HookedMatrix3f {

  HookedMatrix3f m00(float va);

  HookedMatrix3f m01(float va);

  HookedMatrix3f m02(float va);

  HookedMatrix3f m10(float va);

  HookedMatrix3f m11(float va);

  HookedMatrix3f m12(float va);

  HookedMatrix3f m20(float va);

  HookedMatrix3f m21(float va);

  HookedMatrix3f m22(float va);

  float m00();

  float m01();

  float m02();

  float m10();

  float m11();

  float m12();

  float m20();

  float m21();

  float m22();


}
