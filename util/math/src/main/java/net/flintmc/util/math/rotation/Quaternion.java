package net.flintmc.util.math.rotation;

public interface Quaternion<
    T_Number extends Number, T_Quaternion extends Quaternion<T_Number, T_Quaternion>> {

  T_Number getX();

  T_Quaternion setX(T_Number x);

  T_Number getY();

  T_Quaternion setY(T_Number y);

  T_Number getZ();

  T_Quaternion setZ(T_Number z);

  T_Number getW();

  T_Quaternion setW(T_Number w);

  T_Quaternion rotateLocalX(T_Number ang);

  T_Quaternion rotateLocalX(T_Number ang, T_Quaternion target);

  T_Quaternion rotateLocalY(T_Number ang);

  T_Quaternion rotateLocalY(T_Number ang, T_Quaternion target);

  T_Quaternion rotateLocalZ(T_Number ang);

  T_Quaternion rotateLocalZ(T_Number ang, T_Quaternion target);

  T_Quaternion set(T_Number x, T_Number y, T_Number z, T_Number w);
}
