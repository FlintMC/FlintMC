package net.flintmc.framework.config.internal.generator.method.reference;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.inject.implement.Implement;

import java.util.*;

@Singleton
@Implement(ConfigObjectReference.Parser.class)
public class DefaultConfigObjectReferenceParser implements ConfigObjectReference.Parser {

  private final ConfigObjectReference.Factory factory;
  private final ConfigSerializationService serializationService;
  private final ImplementationGenerator implementationGenerator;

  @Inject
  public DefaultConfigObjectReferenceParser(ConfigObjectReference.Factory factory, ConfigSerializationService serializationService,
                                            ImplementationGenerator implementationGenerator) {
    this.factory = factory;
    this.serializationService = serializationService;
    this.implementationGenerator = implementationGenerator;
  }

  @Override
  public ConfigObjectReference parse(GeneratingConfig generatingConfig, ParsedConfig config, ConfigMethod method) {
    // keys in the config
    Collection<String> pathKeys = new ArrayList<>(Arrays.asList(method.getPathPrefix()));
    pathKeys.add(method.getConfigName());

    // all methods that need to be invoked to get to the actual getter/setter method
    CtMethod[] methodPath;
    CtClass declaringClass;
    // the class which contains the method.getMethodNames()
    CtClass mainClass;

    try {
      methodPath = this.asMethods(generatingConfig, method.getPathPrefix());
      declaringClass = methodPath.length == 0 ? method.getDeclaringClass() : methodPath[methodPath.length - 1].getReturnType();
      mainClass = methodPath.length == 0 ? generatingConfig.getBaseClass() : methodPath[methodPath.length - 1].getReturnType();
    } catch (NotFoundException e) {
      throw new RuntimeException("Failed to generate the references for the config " + generatingConfig.getBaseClass().getName(), e);
    }

    // all methods that belong to this group (e.g. setNAME, getNAME, getNAMEAll)
    Collection<CtMethod> allMethods = new HashSet<>(Arrays.asList(methodPath));

    for (CtMethod ctMethod : mainClass.getMethods()) {
      for (String methodName : method.getMethodNames()) {
        if (ctMethod.getName().equals(methodName)) {
          allMethods.add(ctMethod);
        }
      }
    }

    CtClass declaringImplementation = generatingConfig.getGeneratedImplementation(declaringClass.getName(), declaringClass);

    CtMethod getter = this.getMethod(declaringImplementation, method.getGetterName());
    CtMethod setter = this.getMethod(declaringImplementation, method.getSetterName());

    if (getter == null || setter == null) {
      throw new IllegalArgumentException("Setter/Getter for " + method.getConfigName() + " (" + method.getGetterName() + "/" + method.getSetterName()
          + ") not found in " + declaringImplementation.getName());
    }

    return this.factory.create(
        generatingConfig, config,
        pathKeys.toArray(new String[0]),
        methodPath, allMethods.toArray(new CtMethod[0]),
        getter, setter,
        declaringClass.getName(),
        this.implementationGenerator.getClassLoader(),
        method.getSerializedType()
    );
  }

  @Override
  public Collection<ConfigObjectReference> parseAll(GeneratingConfig generatingConfig, ParsedConfig config) {
    Collection<ConfigObjectReference> references = new HashSet<>();

    for (ConfigMethod method : generatingConfig.getAllMethods()) {
      if (!method.getStoredType().isInterface() || this.serializationService.hasSerializer(method.getStoredType())) {
        references.add(this.parse(generatingConfig, config, method));
      }
    }

    return references;
  }

  private CtMethod[] asMethods(GeneratingConfig config, String[] pathPrefix) throws NotFoundException {
    List<CtMethod> methods = new ArrayList<>();
    CtClass currentClass = config.getGeneratedImplementation(config.getBaseClass().getName());

    for (String prefix : pathPrefix) {
      CtMethod currentMethod = this.getMethod(currentClass, "get" + prefix);
      if (currentMethod == null) {
        continue;
      }

      methods.add(currentMethod);
      currentClass = config.getGeneratedImplementation(currentMethod.getReturnType().getName(), currentMethod.getReturnType());

      if (currentClass.equals(CtClass.voidType)) {
        throw new IllegalArgumentException("Cannot have void as a return type of a getter");
      }
    }

    return methods.toArray(new CtMethod[0]);
  }

  private CtMethod getMethod(CtClass declaringClass, String name) {
    for (CtMethod method : declaringClass.getMethods()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }

}
