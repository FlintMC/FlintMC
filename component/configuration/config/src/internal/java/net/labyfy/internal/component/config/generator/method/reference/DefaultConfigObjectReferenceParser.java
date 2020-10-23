package net.labyfy.internal.component.config.generator.method.reference;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.config.generator.base.ImplementationGenerator;

import java.util.*;

@Singleton
@Implement(ConfigObjectReference.Parser.class)
public class DefaultConfigObjectReferenceParser implements ConfigObjectReference.Parser {

  private final ConfigObjectReference.Factory factory;
  private final ImplementationGenerator implementationGenerator;

  @Inject
  public DefaultConfigObjectReferenceParser(ConfigObjectReference.Factory factory, ImplementationGenerator implementationGenerator) {
    this.factory = factory;
    this.implementationGenerator = implementationGenerator;
  }

  @Override
  public ConfigObjectReference parse(GeneratingConfig config, ConfigMethod method) {
    // keys in the config
    Collection<String> pathKeys = new ArrayList<>(Arrays.asList(method.getPathPrefix()));
    pathKeys.add(method.getConfigName());

    // all methods that need to be invoked to get to the actual getter/setter method
    CtMethod[] methodPath;
    CtClass declaringClass;
    try {
      methodPath = this.asMethods(config, method.getPathPrefix());
      declaringClass = methodPath.length == 0 ? method.getDeclaringClass() : methodPath[methodPath.length - 1].getReturnType();
    } catch (NotFoundException e) {
      throw new RuntimeException("Failed to generate the references for the config " + config.getBaseClass().getName(), e);
    }

    // the class which contains the possibleMethodNames
    CtClass mainClass = methodPath.length > 0 ? methodPath[methodPath.length - 1].getDeclaringClass() : config.getBaseClass();
    // all methods that belong to this group (e.g. setNAME, getNAME, getNAMEAll)
    Collection<CtMethod> allMethods = new HashSet<>(Arrays.asList(methodPath));

    for (String methodName : method.getMethodNames()) {
      for (CtMethod ctMethod : mainClass.getMethods()) {
        if (ctMethod.getName().equals(methodName)) {
          allMethods.add(ctMethod);
        }
      }
    }

    CtClass declaringImplementation = config.getGeneratedImplementation(declaringClass.getName());
    if (declaringImplementation == null) {
      declaringImplementation = declaringClass;
    }

    CtMethod getter = this.getMethod(declaringImplementation, method.getGetterName());
    CtMethod setter = this.getMethod(declaringImplementation, method.getSetterName());

    return this.factory.create(pathKeys.toArray(new String[0]),
        methodPath, allMethods.toArray(new CtMethod[0]),
        getter, setter,
        this.implementationGenerator.getClassLoader(),
        method.getSerializedType(config)
    );
  }

  @Override
  public Collection<ConfigObjectReference> parseAll(GeneratingConfig config) {
    Collection<ConfigObjectReference> references = new HashSet<>();

    for (ConfigMethod method : config.getAllMethods()) {
      if (!method.getStoredType().isInterface() || method.isSerializableInterface()) {
        references.add(this.parse(config, method));
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
      currentClass = config.getGeneratedImplementation(currentMethod.getReturnType().getName());
      if (currentClass == null) {
        currentClass = currentMethod.getReturnType();
      }

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
