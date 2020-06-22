package net.labyfy.component.annotation.processing.util;

import net.labyfy.component.annotation.processing.ProcessingException;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Array;

public class ClassUtils {
  public static Class<?> toPrimitiveIfPossible(Class<?> wrapper) {
    if (wrapper.isPrimitive()) {
      return wrapper;
    }

    if (Byte.class.equals(wrapper)) {
      return byte.class;
    } else if (Short.class.equals(wrapper)) {
      return short.class;
    } else if (Integer.class.equals(wrapper)) {
      return int.class;
    } else if (Long.class.equals(wrapper)) {
      return long.class;
    } else if (Float.class.equals(wrapper)) {
      return float.class;
    } else if (Double.class.equals(wrapper)) {
      return double.class;
    } else if (Character.class.equals(wrapper)) {
      return char.class;
    } else if (Boolean.class.equals(wrapper)) {
      return boolean.class;
    }

    return wrapper;
  }

  public static Class<?> toWrapperIfPossible(Class<?> primitive) {
    if (!primitive.isPrimitive()) {
      return primitive;
    }

    if (byte.class.equals(primitive)) {
      return Byte.class;
    } else if (short.class.equals(primitive)) {
      return Short.class;
    } else if (int.class.equals(primitive)) {
      return Integer.class;
    } else if (long.class.equals(primitive)) {
      return Long.class;
    } else if (float.class.equals(primitive)) {
      return Float.class;
    } else if (double.class.equals(primitive)) {
      return Double.class;
    } else if (char.class.equals(primitive)) {
      return Character.class;
    } else if (boolean.class.equals(primitive)) {
      return Boolean.class;
    } else if (void.class.equals(primitive)) {
      return Void.class;
    }

    throw new AssertionError("Couldn't map primitive class " + primitive.getName() + " to wrapper");
  }

  public static Class<?> classFor(TypeMirror mirror) {
    switch (mirror.getKind()) {
      case BYTE:
        return byte.class;
      case SHORT:
        return short.class;
      case INT:
        return int.class;
      case LONG:
        return long.class;
      case FLOAT:
        return float.class;
      case DOUBLE:
        return double.class;
      case CHAR:
        return char.class;
      case BOOLEAN:
        return boolean.class;
      case VOID:
        return void.class;
      case NULL:
        return Object.class;
      case DECLARED:
        String className = ((TypeElement) ((DeclaredType) mirror).asElement()).getQualifiedName().toString();
        Class<?> clazz;

        try {
          clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
          throw new ProcessingException(
              "Tried to load class " + className + " which is not available on the annotation processor classpath", e);
        }

        return clazz;
      case ARRAY:
        return Array.newInstance(classFor(((ArrayType) mirror).getComponentType()), 0).getClass();
      case EXECUTABLE:
        return classFor(((ExecutableType) mirror).getReturnType());
      default:
        throw new ProcessingException(
            "Tried to get class for non class mirror of kind " + mirror.getKind().name());
    }
  }
}
