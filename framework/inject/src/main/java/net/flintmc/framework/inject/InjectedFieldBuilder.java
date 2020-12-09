package net.flintmc.framework.inject;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface InjectedFieldBuilder {

  InjectedFieldBuilder target(CtClass target);

  InjectedFieldBuilder inject(Class<?> type);

  InjectedFieldBuilder inject(CtClass type);

  InjectedFieldBuilder inject(String typeName);

  InjectedFieldBuilder fieldName(String fieldName);

  InjectedFieldBuilder singletonInstance();

  InjectedFieldBuilder notStatic();

  // may also be called multiple times per builder instance, but if fieldName is set and
  // singletonInstance() hasn't been called, it needs to be changed because the same field cannot
  // exist multiple times
  CtField generate() throws CannotCompileException;

  @AssistedFactory(InjectedFieldBuilder.class)
  interface Factory {

    InjectedFieldBuilder create();
  }
}
