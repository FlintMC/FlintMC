package net.labyfy.internal.component.config.generator.method.reference;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.IncludeStorage;
import net.labyfy.component.config.generator.ConfigAnnotationCollector;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Implement(ConfigObjectReference.class)
public class DefaultConfigObjectReference implements ConfigObjectReference {

  private static final Map<Class<?>, Function<Number, Number>> NUMBER_MAPPINGS = new HashMap<>();

  static {
    NUMBER_MAPPINGS.put(Float.class, Number::floatValue);
    NUMBER_MAPPINGS.put(Double.class, Number::doubleValue);
    NUMBER_MAPPINGS.put(Byte.class, Number::byteValue);
    NUMBER_MAPPINGS.put(Short.class, Number::shortValue);
    NUMBER_MAPPINGS.put(Integer.class, Number::intValue);
    NUMBER_MAPPINGS.put(Long.class, Number::intValue);
  }

  private final ConfigAnnotationCollector annotationCollector;
  private final String key;
  private final String[] pathKeys;
  private final CtMethod[] ctPath;
  private final CtMethod[] correspondingCtMethods;
  private final CtMethod ctGetter;
  private final CtMethod ctSetter;
  private final Map<Class<? extends Annotation>, Annotation> lastAnnotations;
  private final ClassLoader classLoader;
  private final Type serializedType;

  private Method[] path;
  private Method getter;
  private Method setter;
  private Method[] correspondingMethods;
  private Collection<Annotation> allAnnotations;

  @AssistedInject
  private DefaultConfigObjectReference(ConfigAnnotationCollector annotationCollector,
                                       @Assisted("pathKeys") String[] pathKeys, @Assisted("path") CtMethod[] ctPath,
                                       @Assisted("correspondingMethods") CtMethod[] correspondingCtMethods,
                                       @Assisted("getter") CtMethod getter, @Assisted("setter") CtMethod setter,
                                       @Assisted("classLoader") ClassLoader classLoader,
                                       @Assisted("serializedType") Type serializedType) {
    this.annotationCollector = annotationCollector;
    this.pathKeys = pathKeys;
    this.key = String.join(".", pathKeys);
    this.ctPath = ctPath;
    this.correspondingCtMethods = correspondingCtMethods;
    this.ctGetter = getter;
    this.ctSetter = setter;
    this.classLoader = classLoader;
    this.serializedType = serializedType;
    this.lastAnnotations = new HashMap<>();
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public String[] getPathKeys() {
    return this.pathKeys;
  }

  @Override
  public Type getSerializedType() {
    return this.serializedType;
  }

  @Override
  public <A extends Annotation> A findLastAnnotation(Class<? extends A> annotationType) {
    if (this.lastAnnotations.containsKey(annotationType)) {
      return (A) this.lastAnnotations.get(annotationType);
    }

    this.mapCorrespondingMethods();

    A annotation = this.annotationCollector.findLastAnnotation(this.correspondingMethods, annotationType);

    this.lastAnnotations.put(annotationType, annotation);

    return annotation;
  }

  @Override
  public Collection<Annotation> findAllAnnotations() {
    if (this.allAnnotations != null) {
      return this.allAnnotations;
    }

    this.mapCorrespondingMethods();

    return this.allAnnotations =
        Collections.unmodifiableCollection(this.annotationCollector.findAllAnnotations(this.correspondingMethods));
  }

  private void mapCorrespondingMethods() {
    if (this.correspondingMethods == null) {
      try {
        this.correspondingMethods = this.mapMethods(this.correspondingCtMethods);
      } catch (ClassNotFoundException | NotFoundException | NoSuchMethodException e) {
        throw new RuntimeException("Failed to map the corresponding CtMethods of the reference for '" + this.key
            + "' to java reflect methods", e);
      }
    }
  }

  @Override
  public boolean appliesTo(ConfigStorage storage) {
    String name = storage.getName();

    IncludeStorage annotation = this.findLastAnnotation(IncludeStorage.class);
    ExcludeStorage excludeAnnotation = this.findLastAnnotation(ExcludeStorage.class);
    String[] included = annotation != null ? annotation.value() : new String[0];
    String[] excluded = excludeAnnotation != null ? excludeAnnotation.value() : new String[0];

    if (excluded.length != 0) {
      for (String s : excluded) {
        if (s.equals(name)) {
          return false;
        }
      }

      return true;
    }

    if (included.length != 0) {
      for (String s : included) {
        if (s.equals(name)) {
          return true;
        }
      }
      return false;
    }

    return true;
  }

  @Override
  public Object getValue(ParsedConfig config) throws InvocationTargetException, IllegalAccessException {
    Object lastInstance = this.getLastInstance(config);
    if (lastInstance == null) {
      return null;
    }

    if (this.getter == null) {
      try {
        this.getter = this.mapMethod(this.ctGetter);
      } catch (ClassNotFoundException | NotFoundException | NoSuchMethodException e) {
        throw new RuntimeException("Failed to map the getter CtMethod of the reference for '" + this.key
            + "' to java reflect methods", e);
      }
    }

    return this.getter.invoke(lastInstance);
  }

  @Override
  public void setValue(ParsedConfig config, Object value) throws InvocationTargetException, IllegalAccessException {
    Object lastInstance = this.getLastInstance(config);
    if (lastInstance == null) {
      return;
    }

    if (this.setter == null) {
      try {
        this.setter = this.mapMethod(this.ctSetter);
      } catch (ClassNotFoundException | NotFoundException | NoSuchMethodException e) {
        throw new RuntimeException("Failed to map the setter CtMethod of the reference for '" + this.key
            + "' to java reflect methods", e);
      }
    }

    Object castedValue = value;
    // map e.g. int to double because Java reflections can't handle it
    if (value instanceof Number) {
      Class<?> type = this.setter.getParameterTypes()[0];
      if (type.isPrimitive()) {
        type = PrimitiveTypeLoader.getPrimitiveClass(type);
      }

      if (Number.class.isAssignableFrom(type) && NUMBER_MAPPINGS.containsKey(type)) {
        castedValue = NUMBER_MAPPINGS.get(type).apply((Number) value);
      }
    }
    this.setter.invoke(lastInstance, castedValue);
  }

  private Object getLastInstance(Object baseInstance) throws InvocationTargetException, IllegalAccessException {
    if (this.path == null) {
      try {
        this.path = this.mapMethods(this.ctPath);
      } catch (ClassNotFoundException | NotFoundException | NoSuchMethodException e) {
        throw new RuntimeException("Failed to map the path CtMethods of the reference for '" + this.key
            + "' to java reflect methods", e);
      }
    }

    if (this.path.length == 0) {
      return baseInstance;
    }

    Object currentInstance = baseInstance;

    for (Method method : this.path) {
      if (currentInstance == null) {
        return null;
      }

      currentInstance = method.invoke(currentInstance);
    }

    return currentInstance;
  }

  private Method[] mapMethods(CtMethod[] ctMethods) throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
    Method[] methods = new Method[ctMethods.length];

    for (int i = 0; i < ctMethods.length; i++) {
      methods[i] = this.mapMethod(ctMethods[i]);
    }

    return methods;
  }

  private Method mapMethod(CtMethod ctMethod) throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
    Class<?> declaringClass = this.classLoader.loadClass(ctMethod.getDeclaringClass().getName());

    CtClass[] ctParameters = ctMethod.getParameterTypes();
    Class<?>[] parameters = new Class[ctParameters.length];

    for (int j = 0; j < ctParameters.length; j++) {
      String name = ctParameters[j].getName();
      parameters[j] = PrimitiveTypeLoader.loadClass(this.classLoader, name);
    }

    return declaringClass.getMethod(ctMethod.getName(), parameters);
  }

}
