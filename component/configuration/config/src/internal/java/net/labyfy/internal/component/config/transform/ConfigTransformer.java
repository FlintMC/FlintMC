package net.labyfy.internal.component.config.transform;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javassist.*;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.config.generator.ConfigImplementer;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@Singleton
@Service(value = ConfigImplementation.class, priority = 2 /* needs to be called after the ConfigGenerationService */)
public class ConfigTransformer implements ServiceHandler<ConfigImplementation> {

  private final ClassPool pool;
  private final ConfigImplementer configImplementer;

  private final Collection<ConfigMethod> pendingTransforms;
  private final Collection<TransformedConfigMeta> mappings;
  private final Map<String, String> launchArguments;

  @Inject
  public ConfigTransformer(ConfigImplementer configImplementer, @Named("launchArguments") Map launchArguments) {
    this.pool = ClassPool.getDefault();

    this.configImplementer = configImplementer;
    this.launchArguments = launchArguments;

    this.pendingTransforms = new HashSet<>();
    this.mappings = new HashSet<>();
  }

  public Collection<TransformedConfigMeta> getMappings() {
    return this.mappings;
  }

  public void addPendingTransform(ConfigMethod method) {
    this.pendingTransforms.add(method);
  }

  @ClassTransform
  public void transformConfigImplementation(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    // implement methods in classes of the config (including the class annotated with @Config) that are also half-generated
    CtClass implementation = context.getCtClass();

    this.implementMethods(implementation);
  }

  @Override
  public void discover(AnnotationMeta<ConfigImplementation> meta) throws ServiceNotFoundException {
    // implement the configs that are annotated with ImplementedConfig
    CtClass implementation = (CtClass) meta.getIdentifier().getLocation();
    ConfigImplementation annotation = meta.getAnnotation();
    String version = annotation.version();

    if (!annotation.value().isAnnotationPresent(ImplementedConfig.class)) {
      throw new ServiceNotFoundException("Cannot use @ConfigImplementation annotation without @ImplementedConfig on its value() ["
          + implementation.getName() + "/" + annotation.value().getName() + "]");
    }

    if (!version.isEmpty() && !launchArguments.get("--game-version").equals(version)) {
      return;
    }

    try {
      TransformedConfigMeta configMeta = new TransformedConfigMeta(annotation.value());
      this.mappings.add(configMeta);

      // load the class so that the transformer will be called
      Class<?> definedImplementation = ConfigTransformer.class.getClassLoader().loadClass(implementation.getName());
      configMeta.setImplementationClass(definedImplementation);

      if (configMeta.getConfig() == null) {
        return;
      }

      // get the new implementation with the changes from the class transformer
      CtClass newImplementation = ClassPool.getDefault().get(implementation.getName());

      // bind the implementation in the config to be used by the ConfigObjectReference.Parser
      configMeta.getConfig().bindGeneratedImplementation(this.pool.get(configMeta.getSuperClass().getName()), newImplementation);
    } catch (ClassNotFoundException | NotFoundException e) {
      throw new ServiceNotFoundException("Cannot load transformed config class");
    }
  }

  private void implementMethods(CtClass implementation) throws CannotCompileException, NotFoundException {
    if (this.pendingTransforms.isEmpty()) {
      return;
    }

    boolean modified = false;

    Collection<ConfigMethod> copy = new HashSet<>(this.pendingTransforms);
    for (ConfigMethod method : copy) {
      CtClass declaring = method.getDeclaringClass();
      if (!implementation.subtypeOf(declaring)) {
        continue;
      }

      if (!modified && implementation.isInterface() && implementation.getName().equals(method.getConfig().getBaseClass().getName())) {
        // add the ParsedConfig interface to the config interface so that guice will also proxy this one and not only the config itself
        implementation.addInterface(this.pool.get(ParsedConfig.class.getName()));
      }

      if (!implementation.isInterface()) {
        if (!modified) {
          implementation.addField(CtField.make("private final transient " + ConfigStorageProvider.class.getName() + " configStorageProvider = "
              + InjectionHolder.class.getName() + ".getInjectedInstance(" + ConfigStorageProvider.class.getName() + ".class);", implementation));

          for (TransformedConfigMeta meta : this.mappings) {
            if (meta.getConfig() == null && meta.getSuperClass().getName().equals(declaring.getName())) {
              meta.setConfig(method.getConfig());
            }
          }
        }

        if (method.getDeclaringClass().getName().equals(method.getConfig().getBaseClass().getName()) &&
            Arrays.stream(implementation.getInterfaces()).noneMatch(iface -> iface.getName().equals(ParsedConfig.class.getName()))) {
          // only the base class annotated with @Config should have the ParsedConfig implementation
          this.configImplementer.implementParsedConfig(implementation, method.getConfig().getName());
        }
      }

      if (implementation.isInterface()) {
        method.addInterfaceMethods(implementation);
      } else {
        method.implementExistingMethods(implementation);
      }

      if (method.hasAddedInterfaceMethods() && method.hasImplementedExistingMethods()) {
        // everything done, remove the method
        this.pendingTransforms.remove(method);
      }

      modified = true;
    }
  }
}
