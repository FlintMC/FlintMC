package net.flintmc.framework.config.internal.generator.method.reference;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.ExcludeStorage;
import net.flintmc.framework.config.annotation.IncludeStorage;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.internal.generator.method.reference.invoker.ReferenceInvocationGenerator;
import net.flintmc.framework.config.internal.generator.method.reference.invoker.ReferenceInvoker;
import net.flintmc.framework.config.modifier.ConfigModifierRegistry;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

@Implement(ConfigObjectReference.class)
public class DefaultConfigObjectReference implements ConfigObjectReference {

  private final ParsedConfig config;

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

  private final Object defaultValue;

  private final Class<?> configBaseClass;
  private final ReferenceInvoker invoker;
  private final Class<?> declaringClass;

  private Method[] correspondingMethods;
  private Collection<Annotation> allAnnotations;

  @AssistedInject
  private DefaultConfigObjectReference(
      EventBus eventBus,
      ConfigValueUpdateEvent.Factory eventFactory,
      ConfigModifierRegistry modifierRegistry,
      ConfigAnnotationCollector annotationCollector,
      ReferenceInvocationGenerator invocationGenerator,
      DefaultAnnotationMapperRegistry defaultAnnotationMapperRegistry,
      @Assisted GeneratingConfig generatingConfig,
      @Assisted ParsedConfig config,
      @Assisted("pathKeys") String[] pathKeys,
      @Assisted("path") CtMethod[] path,
      @Assisted("correspondingMethods") CtMethod[] correspondingCtMethods,
      @Assisted("getter") CtMethod getter,
      @Assisted("setter") CtMethod setter,
      @Assisted("declaringClass") String declaringClass,
      @Assisted("classLoader") ClassLoader classLoader,
      @Assisted("serializedType") Type serializedType)
      throws ReflectiveOperationException, CannotCompileException, NotFoundException, IOException {
    this.config = config;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.modifierRegistry = modifierRegistry;
    this.annotationCollector = annotationCollector;
    this.pathKeys = pathKeys;
    this.key = String.join(".", pathKeys);
    this.correspondingCtMethods = correspondingCtMethods;
    this.classLoader = classLoader;
    this.serializedType = serializedType;

    this.configBaseClass = classLoader.loadClass(generatingConfig.getBaseClass().getName());
    this.declaringClass = classLoader.loadClass(declaringClass);
    this.invoker = invocationGenerator.generateInvoker(generatingConfig, path, getter, setter);

    this.lastAnnotations = new HashMap<>();

    Object defaultValue = null;
    for (Class<? extends Annotation> annotationType :
        defaultAnnotationMapperRegistry.getAnnotationTypes()) {
      Annotation annotation = this.findLastAnnotation(annotationType);
      if (annotation != null) {
        defaultValue = defaultAnnotationMapperRegistry.getDefaultValue(this, annotation);
        break;
      }
    }

    this.defaultValue = defaultValue;
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> getConfigBaseClass() {
    return this.configBaseClass;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return this.key;
  }

  /** {@inheritDoc} */
  @Override
  public String getLastName() {
    return this.pathKeys[this.pathKeys.length - 1];
  }

  /** {@inheritDoc} */
  @Override
  public String[] getPathKeys() {
    return this.pathKeys;
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> getDeclaringClass() {
    return this.declaringClass;
  }

  /** {@inheritDoc} */
  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }

  /** {@inheritDoc} */
  @Override
  public Type getSerializedType() {
    return this.serializedType;
  }

  /** {@inheritDoc} */
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

    A annotation =
        this.annotationCollector.findLastAnnotation(this.correspondingMethods, annotationType);

    this.lastAnnotations.put(annotationType, annotation);

    return annotation;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Annotation> findAllAnnotations() {
    if (this.allAnnotations != null) {
      return this.modifyAnnotations(this.allAnnotations);
    }

    this.mapCorrespondingMethods();

    Collection<Annotation> annotations =
        this.annotationCollector.findAllAnnotations(this.correspondingMethods);

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
        throw new RuntimeException(
            "Failed to map the corresponding CtMethods of the reference for '"
                + this.key
                + "' to java reflect methods",
            e);
      }
    }
  }

  /** {@inheritDoc} */
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

  /** {@inheritDoc} */
  @Override
  public Object getDefaultValue() {
    return this.defaultValue;
  }

  /** {@inheritDoc} */
  @Override
  public Object getValue() {
    return this.invoker.getValue(this.config);
  }

  /** {@inheritDoc} */
  @Override
  public void setValue(Object value) {
    Object previousValue = this.getValue();
    ConfigValueUpdateEvent event = this.eventFactory.create(this, previousValue, value);

    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
    this.invoker.setValue(this.config, value);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  private Method[] mapMethods(CtMethod[] ctMethods)
      throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
    Method[] methods = new Method[ctMethods.length];

    for (int i = 0; i < ctMethods.length; i++) {
      methods[i] = this.mapMethod(ctMethods[i]);
    }

    return methods;
  }

  private Method mapMethod(CtMethod ctMethod)
      throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
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
    return "DefaultConfigObjectReference("
        + this.declaringClass.getName()
        + " -> "
        + this.key
        + ")";
  }
}
