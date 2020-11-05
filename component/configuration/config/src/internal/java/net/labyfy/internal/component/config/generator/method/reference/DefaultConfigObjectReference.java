package net.labyfy.internal.component.config.generator.method.reference;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.IncludeStorage;
import net.labyfy.component.config.event.ConfigValueUpdateEvent;
import net.labyfy.component.config.generator.ConfigAnnotationCollector;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.modifier.ConfigModifierRegistry;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;
import net.labyfy.internal.component.config.generator.method.reference.invoker.ReferenceInvocationGenerator;
import net.labyfy.internal.component.config.generator.method.reference.invoker.ReferenceInvoker;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
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

  private final EventBus eventBus;
  private final ConfigValueUpdateEvent.Factory eventFactory;

  private final ConfigModifierRegistry modifierRegistry;

  private final ConfigAnnotationCollector annotationCollector;
  private final String key;
  private final String[] pathKeys;
  private final CtMethod[] correspondingCtMethods;
  private final Map<Class<? extends Annotation>, Annotation> lastAnnotations;
  private final ClassLoader classLoader;
  private final Type serializedType;

  private final ReferenceInvoker invoker;
  private final Class<?> declaringClass;

  private Method[] correspondingMethods;
  private Collection<Annotation> allAnnotations;

  @AssistedInject
  private DefaultConfigObjectReference(EventBus eventBus, ConfigValueUpdateEvent.Factory eventFactory,
                                       ConfigModifierRegistry modifierRegistry, ConfigAnnotationCollector annotationCollector,
                                       ReferenceInvocationGenerator invocationGenerator,
                                       @Assisted("config") GeneratingConfig config,
                                       @Assisted("pathKeys") String[] pathKeys, @Assisted("path") CtMethod[] path,
                                       @Assisted("correspondingMethods") CtMethod[] correspondingCtMethods,
                                       @Assisted("getter") CtMethod getter, @Assisted("setter") CtMethod setter,
                                       @Assisted("classLoader") ClassLoader classLoader,
                                       @Assisted("serializedType") Type serializedType)
      throws ReflectiveOperationException, CannotCompileException, NotFoundException, IOException {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.modifierRegistry = modifierRegistry;
    this.annotationCollector = annotationCollector;
    this.pathKeys = pathKeys;
    this.key = String.join(".", pathKeys);
    this.correspondingCtMethods = correspondingCtMethods;
    this.classLoader = classLoader;
    this.serializedType = serializedType;

    this.declaringClass = classLoader.loadClass(getter.getDeclaringClass().getName());
    this.invoker = invocationGenerator.generateInvoker(config, path, getter, setter);

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
  public Class<?> getDeclaringClass() {
    return this.declaringClass;
  }

  @Override
  public Type getSerializedType() {
    return this.serializedType;
  }

  @Override
  public <A extends Annotation> A findLastAnnotation(Class<? extends A> annotationType) {
    if (this.lastAnnotations.containsKey(annotationType)) {
      Annotation annotation = this.lastAnnotations.get(annotationType);
      if (annotation != null) {
        annotation = this.modifierRegistry.modify(this, annotation);
      }
      return (A) annotation;
    }

    this.mapCorrespondingMethods();

    A annotation = this.annotationCollector.findLastAnnotation(this.correspondingMethods, annotationType);

    this.lastAnnotations.put(annotationType, annotation);

    return annotation;
  }

  @Override
  public Collection<Annotation> findAllAnnotations() {
    if (this.allAnnotations != null) {
      return this.modifyAnnotations(this.allAnnotations);
    }

    this.mapCorrespondingMethods();

    Collection<Annotation> annotations = this.annotationCollector.findAllAnnotations(this.correspondingMethods);

    return this.modifyAnnotations(this.allAnnotations = annotations);
  }

  private Collection<Annotation> modifyAnnotations(Collection<Annotation> annotations) {
    if (annotations.isEmpty()) {
      return Collections.emptyList();
    }

    Collection<Annotation> modifiedAnnotations = new ArrayList<>(annotations.size());
    for (Annotation annotation : annotations) {
      modifiedAnnotations.add(this.modifierRegistry.modify(this, annotation));
    }

    return Collections.unmodifiableCollection(modifiedAnnotations);
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
  public Object getValue(ParsedConfig config) {
    return this.invoker.getValue(config);
  }

  @Override
  public void setValue(ParsedConfig config, Object value) {
    Object previousValue = this.getValue(config);
    ConfigValueUpdateEvent event = this.eventFactory.create(this, previousValue, value);

    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
    this.invoker.setValue(config, value);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
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

  @Override
  public String toString() {
    return "DefaultConfigObjectReference(" + this.declaringClass + " -> " + this.key + ")";
  }
}
