package net.flintmc.framework.config.internal.generator.method.group;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.SignatureAttribute;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.internal.generator.method.GenericMethodHelper;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigGetterSetter;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigMultiGetterSetter;
import net.flintmc.framework.config.serialization.ConfigSerializationService;

import java.util.Map;

@Singleton
public class ConfigGetterGroup implements ConfigMethodGroup {

  private final ClassPool pool;
  private final GenericMethodHelper methodHelper;
  private final ConfigSerializationService serializationService;

  @Inject
  private ConfigGetterGroup(
      GenericMethodHelper methodHelper, ConfigSerializationService serializationService) {
    this.methodHelper = methodHelper;
    this.pool = ClassPool.getDefault();
    this.serializationService = serializationService;
  }

  @Override
  public String[] getPossiblePrefixes() {
    return new String[] {"get", "is"};
  }

  @Override
  public ConfigMethod resolveMethod(
      GeneratingConfig config, CtClass type, String entryName, CtMethod method)
      throws NotFoundException {
    CtClass methodType = method.getReturnType();
    if (methodType.equals(CtClass.voidType)) {
      return null;
    }

    CtClass[] parameters = method.getParameterTypes();
    if (parameters.length == 0) {
      if (methodType.getName().equals(Map.class.getName())
          && entryName.startsWith(ConfigMultiGetterSetter.ALL_PREFIX)) {
        return this.methodHelper.generateMultiGetterSetter(
            config,
            type,
            method,
            signature -> (SignatureAttribute.ClassType) signature.getReturnType(),
            entryName.substring(ConfigMultiGetterSetter.ALL_PREFIX.length()));
      }

      return new ConfigGetterSetter(this.serializationService, config, type, entryName, methodType);
    }

    if (parameters.length == 1) {
      if (parameters[0].equals(CtClass.voidType)) {
        return null;
      }

      return new ConfigMultiGetterSetter(
          this.serializationService,
          config,
          type,
          entryName,
          this.pool.get(Map.class.getName()),
          parameters[0],
          methodType);
    }

    throw new IllegalArgumentException("Getter can only have either no or one parameter");
  }
}
