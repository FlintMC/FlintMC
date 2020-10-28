package net.labyfy.internal.component.config.transform;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Singleton
@Service(value = ConfigImplementation.class, priority = 2 /* needs to be called after the ConfigGenerationService */)
public class ConfigTransformer implements ServiceHandler<ConfigImplementation> {

  private final Collection<ConfigMethod> pendingTransforms;
  private final Map<Class<?>, Class<?>> mappings;
  private final Map<String, String> launchArguments;

  @Inject
  public ConfigTransformer(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;

    this.pendingTransforms = new HashSet<>();
    this.mappings = new HashMap<>();
  }

  public Map<Class<?>, Class<?>> getMappings() {
    return this.mappings;
  }

  public void addPendingTransform(ConfigMethod method) {
    this.pendingTransforms.add(method);
  }

  @ClassTransform
  public void transformConfigImplementation(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    // implement methods in classes of the config (including the class annotated with @Config) that are also half-generated
    CtClass implementation = context.getCtClass();
    if (implementation.isInterface()) {
      return;
    }

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
      Class<?> definedImplementation = ConfigTransformer.class.getClassLoader().loadClass(implementation.getName());
      this.mappings.put(annotation.value(), definedImplementation);
    } catch (ClassNotFoundException e) {
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

      if (!modified) {
        implementation.addField(CtField.make("private final transient " + ConfigStorageProvider.class.getName() + " configStorageProvider = "
            + InjectionHolder.class.getName() + ".getInjectedInstance(" + ConfigStorageProvider.class.getName() + ".class);", implementation));
      }

      method.implementExistingMethods(implementation);
      this.pendingTransforms.remove(method);

      modified = true;
    }
  }
}
