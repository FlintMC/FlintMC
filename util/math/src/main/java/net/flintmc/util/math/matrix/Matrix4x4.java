package net.flintmc.util.math.matrix;

public interface Matrix4x4<
    T_Number extends Number, T_Matrix4 extends Matrix4x4<T_Number, T_Matrix4>> {

  T_Matrix4 rotate(float ang, float x, float y, float z);

  T_Matrix4 rotate(float ang, float x, float y, float z, T_Matrix4 target);

  T_Matrix4 invert();

  T_Matrix4 invert(T_Matrix4 target);

  T_Matrix4 transpose();

  T_Matrix4 transpose(T_Matrix4 target);

  T_Matrix4 copy(T_Matrix4 target);

  T_Matrix4 mul(T_Matrix4 right);

  T_Matrix4 mul(T_Matrix4 right, T_Matrix4 target);

  T_Matrix4 scale(T_Number factor);

  T_Matrix4 scale(T_Number factor, T_Matrix4 target);

  T_Matrix4 scale(T_Number factorX, T_Number factorY, T_Number factorZ);

  T_Matrix4 scale(T_Number factorX, T_Number factorY, T_Number factorZ, T_Matrix4 target);

  T_Matrix4 translate(T_Number x, T_Number y, T_Number z);

  T_Matrix4 translate(T_Number x, T_Number y, T_Number z, T_Matrix4 target);

  T_Number getM00();

  T_Matrix4 setM00(T_Number m00);

  T_Number getM01();

  T_Matrix4 setM01(T_Number m01);

  T_Number getM02();

  T_Matrix4 setM02(T_Number m02);

  T_Number getM03();

  T_Matrix4 setM03(T_Number m03);

  T_Number getM10();

  T_Matrix4 setM10(T_Number m10);

  T_Number getM11();

  T_Matrix4 setM11(T_Number m11);

  T_Number getM12();

  T_Matrix4 setM12(T_Number m12);

  T_Number getM13();

  T_Matrix4 setM13(T_Number m13);

  T_Number getM20();

  T_Matrix4 setM20(T_Number m20);

  T_Number getM21();

  T_Matrix4 setM21(T_Number m21);

  T_Number getM22();

  T_Matrix4 setM22(T_Number m22);

  T_Number getM23();

  T_Matrix4 setM23(T_Number m23);

  T_Number getM30();

  T_Matrix4 setM30(T_Number m30);

  T_Number getM31();

  T_Matrix4 setM31(T_Number m31);

  T_Number getM32();

  T_Matrix4 setM32(T_Number m32);

  T_Number getM33();

  T_Matrix4 setM33(T_Number m33);

  T_Matrix4 setIdentity();

  T_Matrix4 set(
      T_Number m00,
      T_Number m01,
      T_Number m02,
      T_Number m03,
      T_Number m10,
      T_Number m11,
      T_Number m12,
      T_Number m13,
      T_Number m20,
      T_Number m21,
      T_Number m22,
      T_Number m23,
      T_Number m30,
      T_Number m31,
      T_Number m32,
      T_Number m33);
}
