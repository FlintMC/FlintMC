package net.flintmc.framework.config.internal.generator.method.group;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.internal.generator.method.DefaultConfigMethod;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigGetterSetter;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigMultiGetterSetter;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.serialization.ConfigSerializationService;

import java.util.Map;

@Singleton
public class ConfigSetterGroup implements ConfigMethodGroup {

  private final ClassPool pool = ClassPool.getDefault();
  private final ConfigSerializationService serializationService;

  @Inject
  public ConfigSetterGroup(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  @Override
  public String[] getPossiblePrefixes() {
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
        return null;
        // can't implement this here because we don't know the types that are stored in this map, so we
        // just ignore it so that it won't get registered as a normal setter
      }

      return new ConfigGetterSetter(this.serializationService, config, type, entryName, parameters[0]);
    }

    if (parameters.length == 2) {
      return new ConfigMultiGetterSetter(this.serializationService, config, type, entryName,
          this.pool.get(Map.class.getName()), parameters[0], parameters[1]);
    }

    throw new IllegalArgumentException("Setter method " + method.getName() + " in " + type.getName()
        + " doesn't have exactly one parameter");
  }
}
