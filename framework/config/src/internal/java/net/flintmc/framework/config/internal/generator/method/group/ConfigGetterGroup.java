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
public class ConfigGetterGroup implements ConfigMethodGroup {

  private final ClassPool pool = ClassPool.getDefault();
  private final ConfigSerializationService serializationService;

  @Inject
  public ConfigGetterGroup(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  @Override
  public String[] getPossiblePrefixes() {
    return new String[]{"get", "is"};
  }

  @Override
  public DefaultConfigMethod resolveMethod(GeneratingConfig config, CtClass type, String entryName, CtMethod method) throws NotFoundException {
    CtClass methodType = method.getReturnType();
    if (methodType.equals(CtClass.voidType)) {
      return null;
    }

    CtClass[] parameters = method.getParameterTypes();
    if (parameters.length == 0) {
      if (methodType.getName().equals(Map.class.getName()) && entryName.startsWith(ConfigMultiGetterSetter.ALL_PREFIX)) {
        CtClass objectType = this.pool.get(Object.class.getName());
        return new ConfigMultiGetterSetter(this.serializationService, config, type,
            entryName.substring(ConfigMultiGetterSetter.ALL_PREFIX.length()), methodType, objectType, objectType);
      }

      return new ConfigGetterSetter(this.serializationService, config, type, entryName, methodType);
    }

    if (parameters.length == 1) {
      if (parameters[0].equals(CtClass.voidType)) {
        return null;
      }

      return new ConfigMultiGetterSetter(this.serializationService, config, type, entryName,
          this.pool.get(Map.class.getName()), parameters[0], methodType);
    }

    throw new IllegalArgumentException("Getter can only have either no or one parameter");
  }
}
