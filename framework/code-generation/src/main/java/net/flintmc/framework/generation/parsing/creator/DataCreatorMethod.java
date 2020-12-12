package net.flintmc.framework.generation.parsing.creator;

import java.util.Collection;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.DataField;

public interface DataCreatorMethod {

  CtMethod generateImplementation(CtClass implementationClass, String dataImplementationName)
      throws CannotCompileException, NotFoundException;

  Collection<DataField> getTargetDataFields();
}
