package net.flintmc.framework.generation;

import java.util.Collection;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;

public interface DataImplementationGenerator {

  CtClass generateCreatorImplementationClass(
      CtClass creatorInterface,
      Collection<DataCreatorMethod> dataCreatorMethods,
      String dataImplementationName)
      throws CannotCompileException, NotFoundException;

  CtClass generateDataImplementationClass(
      CtClass dataInterface,
      String dataImplementationName,
      Collection<DataField> targetDataFields,
      Collection<DataFieldMethod> dataFieldMethods)
      throws CannotCompileException, NotFoundException;
}
