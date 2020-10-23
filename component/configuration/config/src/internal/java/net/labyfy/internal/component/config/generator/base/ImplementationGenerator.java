package net.labyfy.internal.component.config.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import net.labyfy.component.config.annotation.Implemented;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.generator.method.ConfigMethodResolver;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.internal.component.config.transform.ConfigTransformer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@AutoLoad
public class ImplementationGenerator {

  private final ClassPool pool;
  private final AtomicInteger counter;
  private final Random random;
  private final ConfigMethodResolver methodResolver;

  private final ConfigClassLoader classLoader;
  private final ConfigTransformer transformer;

  @Inject
  public ImplementationGenerator(ConfigMethodResolver methodResolver, ConfigTransformer transformer) {
    this.classLoader = new ConfigClassLoader(ImplementationGenerator.class.getClassLoader());

    this.pool = ClassPool.getDefault();
    this.counter = new AtomicInteger();
    this.random = new Random();
    this.methodResolver = methodResolver;
    this.transformer = transformer;
  }

  public ConfigClassLoader getClassLoader() {
    return this.classLoader;
  }

  public CtClass implementConfig(CtClass type, GeneratingConfig config) throws NotFoundException, CannotCompileException {

    this.methodResolver.resolveMethods(config);

    for (ConfigMethod method : config.getAllMethods()) {
      CtClass declaring = method.getDeclaringClass();
      if (config.getGeneratedImplementation(declaring.getName()) != null || declaring.hasAnnotation(Implemented.class)) {
        this.transformer.addToTransformations(config, method);
        continue;
      }
      CtClass implementation = this.generateImplementation(config, declaring);
      config.bindGeneratedImplementation(declaring.getName(), implementation);
    }

    for (ConfigMethod method : config.getAllMethods()) {
      CtClass implementation = config.getGeneratedImplementation(method.getDeclaringClass().getName());
      if (implementation == null) {
        // the interface is annotated with @Implemented and therefore the implementation already exists
        continue;
      }

      method.generateMethods(implementation, config);
    }

    return config.getGeneratedImplementation(type.getName());
  }

  private CtClass generateImplementation(GeneratingConfig config, CtClass type) throws CannotCompileException {
    CtClass implementation = this.pool.makeClass(type.getSimpleName() + "_" + this.counter.getAndIncrement()
        + "_" + this.random.nextInt(Integer.MAX_VALUE));
    implementation.addInterface(type);

    implementation.addField(CtField.make("private final transient " + ConfigStorageProvider.class.getName() + " configStorageProvider = "
        + InjectionHolder.class.getName() + ".getInjectedInstance(" + ConfigStorageProvider.class.getName() + ".class);", implementation));
    this.buildConstructor(implementation, type, config.getBaseClass());

    return implementation;
  }

  private void buildConstructor(CtClass implementation, CtClass type, CtClass baseClass) throws CannotCompileException {
    String baseField = "private final transient " + baseClass.getName() + " config";
    if (baseClass.getName().equals(type.getName())) {
      baseField += " = this";
    }
    implementation.addField(CtField.make(baseField + ";", implementation));

    if (baseClass.getName().equals(type.getName())) {
      CtConstructor constructor = CtNewConstructor.make("public " + implementation.getName() + "() {}", implementation);

      // add inject annotation for Guice
      AnnotationsAttribute attribute = new AnnotationsAttribute(implementation.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
      attribute.addAnnotation(new Annotation(Inject.class.getName(), implementation.getClassFile().getConstPool()));
      constructor.getMethodInfo().addAttribute(attribute);

      implementation.addConstructor(constructor);
    } else {
      implementation.addConstructor(CtNewConstructor.make(
          "public " + implementation.getSimpleName() + "(" + baseClass.getName() + " type)" +
              " { this.config = type; }",
          implementation
      ));
    }
  }

}
