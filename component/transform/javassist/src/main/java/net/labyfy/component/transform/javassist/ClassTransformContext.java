package net.labyfy.component.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import javassist.*;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Predicate;

@Deprecated
public interface ClassTransformContext {
    /**
     * Retrieves the owner class.
     *
     * @return The owner class.
     */
    Class<?> getOwnerClass();

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
     * @param returnType  A return type.
     * @param name        A method name.
     * @param body        Method source code.
     * @param modifiers   Method access modifiers.
     * @return A method.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers) throws CannotCompileException;

    /**
     * Adds a method to the class.
     *
     * @param src  Method source code.
     * @return A method.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtMethod addMethod(String src) throws CannotCompileException;

    /**
     * Retrieves an owner method by name.
     *
     * @param name  A method name.
     * @param desc  A method descriptor.
     * @return A method.
     * @throws NotFoundException If the method could not be found.
     */
    CtMethod getOwnerMethod(String name, String desc) throws NotFoundException;

    /**
     * Adds a field to the class.
     *
     * @param type       Field type.
     * @param name       Field name.
     * @param modifiers  Field access modifiers.
     * @return A field.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtField addField(Class<?> type, String name, Modifier... modifiers) throws CannotCompileException;

    /**
     * Adds a field to the class.
     *
     * @param type       Field type.
     * @param name       Field name.
     * @param modifiers  Field access modifiers.
     * @return A field.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException;

    /**
     * Adds a field to the class.
     *
     * @param type       Field type.
     * @param name       Field name.
     * @param value      Initial field value.
     * @param modifiers  Field access modifiers.
     * @return A field.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtField addField(Class<?> type, String name, String value, Modifier... modifiers) throws CannotCompileException;

    /**
     * Adds a field to the class.
     *
     * @param type       Field type.
     * @param name       Field name.
     * @param value      Initial field value.
     * @param modifiers  Field access modifiers.
     * @return A field.
     * @throws CannotCompileException If the class transformation failed.
     */
    CtField addField(String type, String name, String value, Modifier... modifiers) throws CannotCompileException;

    /**
     * Retrieves a method by name and descriptor.
     *
     * @param name     Method name.
     * @param classes  Method parameters.
     * @return A method.
     * @throws NotFoundException If the method could not be found.
     */
    CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException;

    /**
     * Retrieves filters.
     *
     * @return Filters.
     */
    Collection<Predicate<CtClass>> getFilters();

    /**
     * Retrieves the owner method.
     *
     * @return An owner method.
     */
    Method getOwnerMethod();

    /**
     * Retrieves the class transformer.
     *
     * @return A class transformer.
     */
    ClassTransform getClassTransform();

    /**
     * Retrieves the owner.
     *
     * @return An owner.
     */
    Object getOwner();

    /**
     * Retrieves the name resolver.
     *
     * @return A name resolver.
     */
    NameResolver getNameResolver();

    /**
     * Retrieves the class.
     *
     * @return A class.
     */
    CtClass getCtClass();

    /**
     * Set the class.
     *
     * @param ctClass A class.
     */
    void setCtClass(CtClass ctClass);

    @AssistedFactory(ClassTransformContext.class)
    interface Factory {
        ClassTransformContext create(
                @Assisted("filters") Collection<Predicate<CtClass>> filters,
                NameResolver nameResolver,
                ClassTransform classTransform,
                Method method,
                Class ownerClass,
                @Assisted("owner") Object owner);
    }
}
