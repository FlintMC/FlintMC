package net.flintmc.framework.config.internal.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodResolver;
import net.flintmc.framework.config.internal.transform.ConfigTransformer;
import net.flintmc.framework.config.internal.transform.PendingTransform;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.inject.InjectedFieldBuilder;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ImplementationGenerator {

  private final ClassPool pool;
  private final AtomicInteger counter;
  private final Random random;
  private final ConfigMethodResolver methodResolver;

  private final InjectedFieldBuilder.Factory fieldBuilderFactory;
  private final ConfigClassLoader classLoader;
  private final ConfigTransformer transformer;

  @Inject
  public ImplementationGenerator(
          ClassPool pool,
          ConfigMethodResolver methodResolver,
      InjectedFieldBuilder.Factory fieldBuilderFactory,
      ConfigTransformer transformer) {
    this.fieldBuilderFactory = fieldBuilderFactory;
    this.classLoader = new ConfigClassLoader(ImplementationGenerator.class.getClassLoader());

    this.pool = pool;
    this.counter = new AtomicInteger();
    this.random = new Random();
    this.methodResolver = methodResolver;
    this.transformer = transformer;
  }

  public ConfigClassLoader getClassLoader() {
    return this.classLoader;
  }

  public CtClass implementConfig(CtClass type, GeneratingConfig config)
      throws NotFoundException, CannotCompileException {
    this.methodResolver.resolveMethods(config);

    for (ConfigMethod method : config.getAllMethods()) {
      // transform all methods
      this.transformer.getPendingTransforms().add(new PendingTransform(method));

      CtClass declaring = method.getDeclaringClass();
      if (declaring.hasAnnotation(ImplementedConfig.class)) {
        continue;
      }

      // generate only the methods in the interface by the transformer (will be added to it below)
      method.requireNoImplementation();

      if (config.getGeneratedImplementation(declaring.getName()) != null) {
        continue;
      }
      CtClass implementation = this.generateImplementation(config, declaring);
      config.bindGeneratedImplementation(declaring, implementation);
    }

    for (ConfigMethod method : config.getAllMethods()) {
      CtClass implementation =
          config.getGeneratedImplementation(method.getDeclaringClass().getName());
      if (implementation == null) {
        // the interface is annotated with @Implemented and therefore the implementation already
        // exists
        continue;
      }

      method.generateMethods(implementation);
    }

    return config.getGeneratedImplementation(type.getName());
  }

  private CtClass generateImplementation(GeneratingConfig config, CtClass type)
      throws CannotCompileException {
    CtClass implementation =
        this.pool.makeClass(
            type.getSimpleName()
                + "_"
                + this.counter.getAndIncrement()
                + "_"
                + this.random.nextInt(Integer.MAX_VALUE));
    implementation.addInterface(type);

    this.addConfigStorageProvider(implementation);
    this.buildConstructor(implementation, type, config.getBaseClass());

    return implementation;
  }

  public void addConfigStorageProvider(CtClass implementation) throws CannotCompileException {
    this.fieldBuilderFactory
        .create()
        .target(implementation)
        .fieldName("configStorageProvider")
        .inject(ConfigStorageProvider.class)
        .generate();
  }

  private void buildConstructor(CtClass implementation, CtClass type, CtClass baseClass)
      throws CannotCompileException {
    String baseField = "private final transient " + baseClass.getName() + " config";
    if (baseClass.getName().equals(type.getName())) {
      baseField += " = this";
    }
    implementation.addField(CtField.make(baseField + ";", implementation));

    if (baseClass.getName().equals(type.getName())) {
      CtConstructor constructor =
          CtNewConstructor.make(
              "public " + implementation.getSimpleName() + "() {}", implementation);

      // add inject annotation for Guice
      AnnotationsAttribute attribute =
          new AnnotationsAttribute(
              implementation.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
      attribute.addAnnotation(
          new Annotation(Inject.class.getName(), implementation.getClassFile().getConstPool()));
      constructor.getMethodInfo().addAttribute(attribute);

      implementation.addConstructor(constructor);
    } else {
      implementation.addConstructor(
          CtNewConstructor.make(
              "public "
                  + implementation.getSimpleName()
                  + "("
                  + baseClass.getName()
                  + " type)"
                  + " { this.config = type; }",
              implementation));
    }
  }
}