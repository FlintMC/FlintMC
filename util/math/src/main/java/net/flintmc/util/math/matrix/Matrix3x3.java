package net.flintmc.util.math.matrix;

public interface Matrix3x3<T_Number extends Number, T_Matrix3 extends Matrix3x3<T_Number, T_Matrix3>> {

  T_Matrix3 invert();

  T_Matrix3 invert(T_Matrix3 target);

  T_Matrix3 transpose();

  T_Matrix3 transpose(T_Matrix3 target);

  T_Matrix3 copy(T_Matrix3 target);

  T_Matrix3 mul(T_Matrix3 right);

  T_Matrix3 mul(T_Matrix3 right, T_Matrix3 target);

  T_Matrix3 scale(T_Number factor);

  T_Matrix3 scale(T_Number factor, T_Matrix3 target);

  T_Matrix3 scale(T_Number factorX, T_Number factorY, T_Number factorZ);

  T_Matrix3 scale(T_Number factorX, T_Number factorY, T_Number factorZ, T_Matrix3 target);

  T_Number getM00();

  T_Matrix3 setM00(T_Number m00);

  T_Number getM01();

  T_Matrix3 setM01(T_Number m01);

  T_Number getM02();

  T_Matrix3 setM02(T_Number m02);

  T_Number getM10();

  T_Matrix3 setM10(T_Number m10);

  T_Number getM11();

  T_Matrix3 setM11(T_Number m11);

  T_Number getM12();

  T_Matrix3 setM12(T_Number m12);

  T_Number getM20();

  T_Matrix3 setM20(T_Number m20);

  T_Number getM21();

  T_Matrix3 setM21(T_Number m21);

  T_Number getM22();

  T_Matrix3 setM22(T_Number m22);

  T_Matrix3 setIdentity();

  T_Matrix3 set(
      T_Number m00,
      T_Number m01,
      T_Number m02,
      T_Number m10,
      T_Number m11,
      T_Number m12,
      T_Number m20,
      T_Number m21,
      T_Number m22);
}
