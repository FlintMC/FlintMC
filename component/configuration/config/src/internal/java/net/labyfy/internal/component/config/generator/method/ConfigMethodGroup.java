package net.labyfy.internal.component.config.generator.method;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.method.ConfigMethod;

public interface ConfigMethodGroup {

  String getPrefix();

  ConfigMethod resolveMethod(CtClass type, String entryName, CtMethod method) throws NotFoundException;

}
