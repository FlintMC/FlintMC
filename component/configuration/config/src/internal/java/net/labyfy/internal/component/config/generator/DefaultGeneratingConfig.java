package net.labyfy.internal.component.config.generator;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CtClass;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.config.generator.base.ImplementationGenerator;

import java.util.*;

@Implement(GeneratingConfig.class)
public class DefaultGeneratingConfig implements GeneratingConfig {

  private final ClassLoader classLoader;
  private final String name;
  private final CtClass baseClass;
  private final Collection<ConfigMethod> allMethods;
  private final Map<String, CtClass> generatedImplementations;

  @AssistedInject
  private DefaultGeneratingConfig(ImplementationGenerator generator, @Assisted("baseClass") CtClass baseClass) {
    this.classLoader = generator.getClassLoader();
    this.name = baseClass.getName();
    this.baseClass = baseClass;
    this.allMethods = new HashSet<>();
    this.generatedImplementations = new HashMap<>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public CtClass getBaseClass() {
    return this.baseClass;
  }

  @Override
  public Collection<ConfigMethod> getAllMethods() {
    return this.allMethods;
  }

  @Override
  public Collection<CtClass> getGeneratedImplementations() {
    return Collections.unmodifiableCollection(this.generatedImplementations.values());
  }

  @Override
  public CtClass getGeneratedImplementation(String baseName) {
    return this.generatedImplementations.get(baseName);
  }

  @Override
  public CtClass getGeneratedImplementation(String baseName, CtClass def) {
    return this.generatedImplementations.getOrDefault(baseName, def);
  }

  @Override
  public void bindGeneratedImplementation(String baseName, CtClass implementation) {
    this.generatedImplementations.put(baseName, implementation);
  }

  @Override
  public ClassLoader getClassLoader() {
    return this.classLoader;
  }

}
