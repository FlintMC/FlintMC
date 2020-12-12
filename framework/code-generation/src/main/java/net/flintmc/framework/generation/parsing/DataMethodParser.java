package net.flintmc.framework.generation.parsing;

import java.util.Collection;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;

public interface DataMethodParser {

  Collection<DataCreatorMethod> parseCreatorMethods(CtClass creatorInterface, CtClass dataInterface)
      throws NotFoundException, ClassNotFoundException;

  Collection<DataFieldMethod> parseDataMethods(
      CtClass dataInterface, Collection<DataField> targetDataFieldsFields)
      throws NotFoundException, ClassNotFoundException;
}
