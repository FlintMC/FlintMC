package net.labyfy.component.annotation.processing.mirror;

import net.labyfy.component.annotation.processing.ProcessorState;
import net.labyfy.component.annotation.processing.util.ClassUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class MirroredAnnotation {
  private final Map<String, MirroredAnnotationValue> values;

  MirroredAnnotation(Map<String, MirroredAnnotationValue> values) {
    this.values = values;
  }

  static MirroredAnnotation mirror(AnnotationMirror mirror) {
    return new MirroredAnnotation(
        ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getElementValuesWithDefaults(mirror)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                (entry) -> entry.getKey().getSimpleName().toString(),
                (entry) -> new MirroredAnnotationValue(entry.getKey(), entry.getValue()))));
  }

  public byte getByte(String name) {
    return getValue(byte.class, name);
  }

  public short getShort(String name) {
    return getValue(short.class, name);
  }

  public int getInt(String name) {
    return getValue(int.class, name);
  }

  public long getLong(String name) {
    return getValue(long.class, name);
  }

  public float getFloat(String name) {
    return getValue(float.class, name);
  }

  public double getDouble(String name) {
    return getValue(double.class, name);
  }

  public boolean getBoolean(String name) {
    return getValue(boolean.class, name);
  }

  public String getString(String name) {
    return getValue(String.class, name);
  }

  public <T> T[] getArray(Class<T> targetType, String name) {
    @SuppressWarnings("unchecked")
    Class<T[]> arrayTargetType = (Class<T[]>) Array.newInstance(targetType, 0).getClass();
    return getValue(arrayTargetType, name);
  }

  public MirroredAnnotation getAnnotation(String name) {
    return mirror(getValue(AnnotationMirror.class, name));
  }

  public List<MirroredAnnotation> getAnnotations(String name) {
    return Arrays.stream(getValue(AnnotationMirror[].class, name))
        .map(MirroredAnnotation::mirror)
        .collect(Collectors.toList());
  }

  public String getEnum(String name) {
    return getValue(VariableElement.class, name).getSimpleName().toString();
  }

  public List<String> getEnums(String name) {
    return Arrays.stream(getValue(VariableElement[].class, name))
        .map(VariableElement::getSimpleName)
        .map(Object::toString)
        .collect(Collectors.toList());
  }

  public TypeMirror getType(String name) {
    return getValue(TypeMirror.class, name);
  }

  public List<TypeMirror> getTypes(String name) {
    return Arrays.asList(getValue(TypeMirror[].class, name));
  }

  public boolean has(String name) {
    return values.containsKey(name);
  }

  private void ensureExists(String name) {
    if (!has(name)) {
      throw new NoSuchElementException("No annotation value named " + name + " found");
    }
  }

  private <T> T getValue(Class<T> targetType, String name) {
    ensureExists(name);

    MirroredAnnotationValue value = values.get(name);
    if (targetType.equals(AnnotationMirror.class)) {
      AnnotationValue annotationValue = value.getValue();
      if (!(annotationValue instanceof AnnotationMirror)) {
        throw new ClassCastException(
            "Tried to get value of " + name + " as annotation mirror, but the member is not an annotation");
      } else {
        return targetType.cast(annotationValue);
      }
    } else if (targetType.equals(VariableElement.class)) {
      Object annotationValue = value.getValue().getValue();
      if (!(annotationValue instanceof VariableElement)) {
        throw new ClassCastException(
            "Tried to get value of " + name + " as variable element, but the member is not an enum");
      } else {
        return targetType.cast(annotationValue);
      }
    } else if (targetType.equals(TypeMirror.class)) {
      Object annotationValue = value.getValue().getValue();
      if (!(annotationValue instanceof TypeMirror)) {
        throw new ClassCastException(
            "Tried to get value of " + name + "  as a declared type, but the member is not a class");
      } else {
        return targetType.cast(annotationValue);
      }
    } else if (targetType.equals(AnnotationMirror[].class)) {
      Object annotationValue = value.getValue().getValue();
      if (!(annotationValue instanceof List)) {
        throw new ClassCastException(
            "Tried to get value of " + name +
                " as an array of annotation mirrors, but the member is not an array of annotations");
      } else {
        @SuppressWarnings("unchecked")
        List<AnnotationValue> mirrors = (List<AnnotationValue>) annotationValue;
        Object output = Array.newInstance(AnnotationMirror.class, mirrors.size());

        for (int i = 0; i < mirrors.size(); i++) {
          Object v = mirrors.get(i).getValue();
          if (!(v instanceof AnnotationMirror)) {
            throw new ClassCastException("Tried to get value of " + name +
                " as an array of annotation mirrors, but the member is not an array of annotations");
          }

          Array.set(output, i, v);
        }

        return targetType.cast(output);
      }
    } else if (targetType.equals(VariableElement[].class)) {
      Object annotationValue = value.getValue().getValue();
      if (!(annotationValue instanceof List)) {
        throw new ClassCastException("Tried to get value of " + name +
            " as an array of variable elements, but the member is not an array of enum values");
      } else {
        @SuppressWarnings("unchecked")
        List<AnnotationValue> elements = (List<AnnotationValue>) annotationValue;
        Object output = Array.newInstance(VariableElement.class, elements.size());

        for (int i = 0; i < elements.size(); i++) {
          Object v = elements.get(i).getValue();
          if (!(v instanceof VariableElement)) {
            throw new ClassCastException("Tried to get value of " + name +
                " as an array of variable elements, but the member is not an array of enum values");
          }

          Array.set(output, i, v);
        }

        return targetType.cast(output);
      }
    } else if (targetType.equals(TypeMirror[].class)) {
      Object annotationValue = value.getValue().getValue();
      if (!(annotationValue instanceof List)) {
        throw new ClassCastException("Tried to get value of " + name +
            " as an array of declared types, but the member is not an array of classes");
      } else {
        @SuppressWarnings("unchecked")
        List<AnnotationValue> types = (List<AnnotationValue>) annotationValue;
        Object output = Array.newInstance(TypeMirror.class, types.size());

        for (int i = 0; i < types.size(); i++) {
          Object v = types.get(i).getValue();
          if (!(v instanceof TypeMirror)) {
            throw new ClassCastException("Tried to get value of " + name +
                " as an array of declared types, but the member is not an array of classes");
          }

          Array.set(output, i, v);
        }

        return targetType.cast(output);
      }
    }

    ExecutableElement key = value.getKey();

    Class<?> augmented = ClassUtils.toPrimitiveIfPossible(targetType);
    Class<?> real = ClassUtils.classFor(key.asType());
    if (!augmented.isAssignableFrom(real)) {
      throw new ClassCastException(
          "Can not cast " + real.getName() + " to " + targetType.getName() + " (" + augmented.getName() + ")");
    }

    return extractValue(targetType, value.getValue());
  }

  @SuppressWarnings("unchecked")
  private <T> T extractValue(Class<T> targetType, AnnotationValue annotationValue) {
    if ((annotationValue.getValue()) instanceof List && targetType.isArray()) {
      List<AnnotationValue> values = (List<AnnotationValue>) annotationValue.getValue();
      Object output = Array.newInstance(targetType.getComponentType(), values.size());
      for (int i = 0; i < values.size(); i++) {
        Array.set(output, i, extractValue(targetType.getComponentType(), values.get(i)));
      }

      return targetType.cast(output);
    } else if (annotationValue instanceof VariableElement && targetType.isEnum()) {
      Object value = ((VariableElement) annotationValue.getValue()).getConstantValue();
      return targetType.cast(value);
    } else {
      Object value = annotationValue.getValue();
      if (annotationValue == null) {
        if (targetType.isPrimitive()) {
          throw new ClassCastException("Tried to cast null to primitive " + targetType.getName());
        } else {
          return null;
        }
      }

      return (T) ClassUtils.toWrapperIfPossible(targetType).cast(value);
    }
  }
}
