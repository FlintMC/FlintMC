package net.labyfy.internal.component.config.generator.method.defaults;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.internal.component.config.generator.method.ConfigMethodGroup;
import net.labyfy.internal.component.config.generator.method.DefaultConfigMethod;

import java.util.Map;

public class ConfigSetterGroup implements ConfigMethodGroup {

  private final ClassPool pool = ClassPool.getDefault();

  @Override
  public String[] getPrefix() {
    return new String[]{"set"};
  }

  @Override
  public DefaultConfigMethod resolveMethod(GeneratingConfig config, CtClass type, String entryName, CtMethod method) throws NotFoundException {
    if (!method.getReturnType().equals(CtClass.voidType)) {
      throw new IllegalArgumentException("Setter method " + method.getName() + " in " + type.getName()
          + " doesn't have void as its return type");
    }

    CtClass[] parameters = method.getParameterTypes();
    for (CtClass parameter : parameters) {
      if (parameter.equals(CtClass.voidType)) {
        return null;
      }
    }

    if (parameters.length == 1) {
      if (parameters[0].getName().equals(Map.class.getName()) && entryName.startsWith(ConfigMultiGetterSetter.ALL_PREFIX)) {
        return null; // can't implement this here because we don't know the types that are in this map, so we
        // just ignore it so that it won't get registered as a normal setter
      }

      return new ConfigGetterSetter(config, type, entryName, parameters[0]);
    }

    if (parameters.length == 2) {
      return new ConfigMultiGetterSetter(config, type, entryName, this.pool.get(Map.class.getName()), parameters[0], parameters[1]);
    }

    throw new IllegalArgumentException("Setter method " + method.getName() + " in " + type.getName()
        + " doesn't have exactly one parameter");
  }
}
