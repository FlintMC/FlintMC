package net.labyfy.internal.component.config.generator.method;

import com.google.inject.Inject;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.generator.method.ConfigMethodResolver;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.config.generator.method.defaults.ConfigGetterGroup;
import net.labyfy.internal.component.config.generator.method.defaults.ConfigSetterGroup;

import java.util.Arrays;
import java.util.Collection;

@Implement(ConfigMethodResolver.class)
public class DefaultConfigMethodResolver implements ConfigMethodResolver {

  private final Collection<ConfigMethodGroup> groups;

  @Inject
  public DefaultConfigMethodResolver() {
    this.groups = Arrays.asList(
        new ConfigGetterGroup(),
        new ConfigSetterGroup()
    );
  }

  @Override
  public void resolveMethods(GeneratingConfig config) throws NotFoundException {
    this.resolveMethods(config.getBaseClass(), new String[0], config.getAllMethods());
  }

  private void resolveMethods(CtClass type, String[] prefix, Collection<ConfigMethod> methods) throws NotFoundException {
    for (CtMethod method : type.getMethods()) {
      if (!method.isEmpty()) { // default implementation in the interface
        continue;
      }

      if (method.getDeclaringClass().getName().equals(Object.class.getName())) {
        continue;
      }

      String name = method.getName();

      for (ConfigMethodGroup group : this.groups) {
        if (name.startsWith(group.getPrefix())) {
          String entryName = name.substring(group.getPrefix().length());

          ConfigMethod configMethod = group.resolveMethod(type, entryName, method);

          if (configMethod != null && !this.containsMethod(prefix, configMethod.getConfigName(), methods)) {
            configMethod.setPathPrefix(prefix);

            methods.add(configMethod);

            for (CtClass subType : configMethod.getTypes()) {
              if (subType.isInterface() && !configMethod.isSerializableInterface(subType)
                  && method.getParameterTypes().length == 0) {
                String[] newPrefix = Arrays.copyOf(prefix, prefix.length + 1);
                newPrefix[newPrefix.length - 1] = configMethod.getConfigName();

                this.resolveMethods(subType, newPrefix, methods);
              }
            }

            break;
          }
        }
      }
    }
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
