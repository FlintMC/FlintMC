package net.labyfy.internal.component.config.generator.method.group;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;

public interface ConfigMethodGroup {

  String[] getPossiblePrefixes();

  ConfigMethod resolveMethod(GeneratingConfig config, CtClass type, String entryName, CtMethod method) throws NotFoundException;

}
