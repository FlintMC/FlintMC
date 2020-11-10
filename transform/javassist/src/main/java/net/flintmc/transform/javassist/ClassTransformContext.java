package net.flintmc.transform.javassist;

import javassist.*;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface ClassTransformContext {

  /**
   * Retrieves a field by name.
   *
   * @param name A field name.
   * @return A field.
   * @throws NotFoundException If the field could not be found.
   */
  CtField getField(String name) throws NotFoundException;

  /**
   * Adds a method to the class.
   *
   * @param returnType A return type.
   * @param name A method name.
   * @param body Method source code.
   * @param modifiers Method access modifiers.
   * @return A method.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Adds a method to the class.
   *
   * @param src Method source code.
   * @return A method.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtMethod addMethod(String src) throws CannotCompileException;

  /**
   * Retrieves an owner method by name.
   *
   * @param name A method name.
   * @param desc A method descriptor.
   * @return A method.
   * @throws NotFoundException If the method could not be found.
   */
  CtMethod getOwnerMethod(String name, String desc) throws NotFoundException;

  /**
   * Adds a field to the class.
   *
   * @param type Field type.
   * @param name Field name.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(Class<?> type, String name, Modifier... modifiers) throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type Field type.
   * @param name Field name.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type Field type.
   * @param name Field name.
   * @param value Initial field value.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(Class<?> type, String name, String value, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Adds a field to the class.
   *
   * @param type Field type.
   * @param name Field name.
   * @param value Initial field value.
   * @param modifiers Field access modifiers.
   * @return A field.
   * @throws CannotCompileException If the class transformation failed.
   */
  CtField addField(String type, String name, String value, Modifier... modifiers)
      throws CannotCompileException;

  /**
   * Retrieves a method by name and descriptor.
   *
   * @param name Method name.
   * @param classes Method parameters.
   * @return A method.
   * @throws NotFoundException If the method could not be found.
   */
  CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException;

  /**
   * Retrieves the class.
   *
   * @return A class.
   */
  CtClass getCtClass();

  //@AssistedFactory(ClassTransformContext.class)
  interface Factory {
    ClassTransformContext create(CtClass ctClass);
  }
}
