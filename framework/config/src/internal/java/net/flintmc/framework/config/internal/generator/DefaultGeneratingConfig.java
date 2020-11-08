package net.flintmc.framework.config.internal.generator;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CtClass;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;

import java.util.*;

@Implement(GeneratingConfig.class)
public class DefaultGeneratingConfig implements GeneratingConfig {

  private final ClassLoader classLoader;
  private final String name;
  private final CtClass baseClass;
  private final Collection<ConfigMethod> allMethods;
  private final Collection<CtClass> implementedInterfaces;
  private final Map<String, CtClass> generatedImplementations;

  @AssistedInject
  private DefaultGeneratingConfig(ImplementationGenerator generator, @Assisted("baseClass") CtClass baseClass) {
    this.classLoader = generator.getClassLoader();
    this.name = baseClass.getName();
    this.baseClass = baseClass;
    this.allMethods = new HashSet<>();
    this.implementedInterfaces = new HashSet<>();
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
  public Collection<CtClass> getImplementedInterfaces() {
    return Collections.unmodifiableCollection(this.implementedInterfaces);
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
  public void bindGeneratedImplementation(CtClass base, CtClass implementation) {
    this.generatedImplementations.put(base.getName(), implementation);
    this.implementedInterfaces.add(base);
  }

  @Override
  public ClassLoader getClassLoader() {
    return this.classLoader;
  }

}
