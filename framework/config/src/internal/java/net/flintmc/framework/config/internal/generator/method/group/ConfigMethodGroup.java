package net.flintmc.framework.config.internal.generator.method.group;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;

public interface ConfigMethodGroup {

  String[] getPossiblePrefixes();

  ConfigMethod resolveMethod(GeneratingConfig config, CtClass type, String entryName, CtMethod method) throws NotFoundException;

}
