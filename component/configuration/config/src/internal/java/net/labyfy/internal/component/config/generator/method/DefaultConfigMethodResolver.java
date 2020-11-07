package net.labyfy.internal.component.config.generator.method;

import com.google.inject.Inject;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.ConfigExclude;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.generator.method.ConfigMethodResolver;
import net.labyfy.component.config.serialization.ConfigSerializationService;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.config.generator.method.group.ConfigGetterGroup;
import net.labyfy.internal.component.config.generator.method.group.ConfigMethodGroup;
import net.labyfy.internal.component.config.generator.method.group.ConfigSetterGroup;

import java.util.Arrays;
import java.util.Collection;

@Implement(ConfigMethodResolver.class)
public class DefaultConfigMethodResolver implements ConfigMethodResolver {

  private final ConfigSerializationService serializationService;
  private final Collection<ConfigMethodGroup> groups;

  @Inject
  public DefaultConfigMethodResolver(ConfigSerializationService serializationService, ConfigGetterGroup getterGroup,
                                     ConfigSetterGroup setterGroup) {
    this.serializationService = serializationService;
    this.groups = Arrays.asList(getterGroup, setterGroup);
  }

  @Override
  public void resolveMethods(GeneratingConfig config) throws NotFoundException {
    this.resolveMethods(config, config.getBaseClass(), new String[0]);
  }

  private void resolveMethods(GeneratingConfig config, CtClass type, String[] prefix) throws NotFoundException {
    for (CtMethod method : type.getMethods()) {
      if (!method.isEmpty()) { // default implementation in the interface
        continue;
      }

      if (method.getDeclaringClass().getName().equals(Object.class.getName())) {
        continue;
      }
      if (method.hasAnnotation(ConfigExclude.class)) {
        continue;
      }

      String name = method.getName();

      this.tryGroups(config, name, type, prefix, method);
    }
  }

  private void tryGroups(GeneratingConfig config, String name, CtClass type, String[] prefix,
                         CtMethod method) throws NotFoundException {
    for (ConfigMethodGroup group : this.groups) {
      for (String groupPrefix : group.getPossiblePrefixes()) {
        if (name.startsWith(groupPrefix)) {
          String entryName = name.substring(groupPrefix.length());
          if (this.handleGroup(config, group, type, prefix, entryName, method)) {
            return;
          }

          break;
        }
      }
    }
  }

  private boolean handleGroup(GeneratingConfig config, ConfigMethodGroup group, CtClass type, String[] prefix,
                              String entryName, CtMethod method) throws NotFoundException {
    Collection<ConfigMethod> methods = config.getAllMethods();
    ConfigMethod configMethod = group.resolveMethod(config, type, entryName, method);

    if (configMethod != null && !this.containsMethod(prefix, configMethod.getConfigName(), methods)) {
      configMethod.setPathPrefix(prefix);

      methods.add(configMethod);

      for (CtClass subType : configMethod.getTypes()) {
        if (subType.isInterface() && !this.serializationService.hasSerializer(subType)
            && method.getParameterTypes().length == 0) {
          String[] newPrefix = Arrays.copyOf(prefix, prefix.length + 1);
          newPrefix[newPrefix.length - 1] = configMethod.getConfigName();

          this.resolveMethods(config, subType, newPrefix);
        }
      }

      return true;
    }

    return false;
  }

  private boolean containsMethod(String[] prefix, String name, Collection<ConfigMethod> methods) {
    for (ConfigMethod method : methods) {
      if (Arrays.equals(prefix, method.getPathPrefix()) && method.getConfigName().equals(name)) {
        return true;
      }
    }
    return false;
  }

}
