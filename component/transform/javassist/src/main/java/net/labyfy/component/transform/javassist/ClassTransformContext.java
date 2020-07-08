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
     * Get owner class.
     *
     * @return the owner class.
     */
    Class<?> getOwnerClass();

    /**
     * Get a field by name.
     *
     * @param name  a field name.
     * @return a field.
     * @throws NotFoundException if the field could not be found.
     */
    CtField getField(String name) throws NotFoundException;

    /**
     * Add method to the class.
     *
     * @param returnType  a return type.
     * @param name        a method name.
     * @param body        method source code.
     * @param modifiers   method access modifiers.
     * @return a method.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers) throws CannotCompileException;

    /**
     * Add a method to the class.
     *
     * @param src  method source code.
     * @return a method.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtMethod addMethod(String src) throws CannotCompileException;

    /**
     * Get an owner method by name.
     *
     * @param name  a method name.
     * @param desc  a method descriptor.
     * @return a method.
     * @throws NotFoundException if the method could not be found.
     */
    CtMethod getOwnerMethod(String name, String desc) throws NotFoundException;

    /**
     * Add a field to the class.
     *
     * @param type       field type.
     * @param name       field name.
     * @param modifiers  field access modifiers.
     * @return a field.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtField addField(Class<?> type, String name, Modifier... modifiers) throws CannotCompileException;

    /**
     * Add a field to the class.
     *
     * @param type       field type.
     * @param name       field name.
     * @param modifiers  field access modifiers.
     * @return a field.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtField addField(String type, String name, Modifier... modifiers) throws CannotCompileException;

    /**
     * Add a field to the class.
     *
     * @param type       field type.
     * @param name       field name.
     * @param value      initial field value.
     * @param modifiers  field access modifiers.
     * @return a field.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtField addField(Class<?> type, String name, String value, Modifier... modifiers) throws CannotCompileException;

    /**
     * Add a field to the class.
     *
     * @param type       field type.
     * @param name       field name.
     * @param value      initial field value.
     * @param modifiers  field access modifiers.
     * @return a field.
     * @throws CannotCompileException if the class transformation failed.
     */
    CtField addField(String type, String name, String value, Modifier... modifiers) throws CannotCompileException;

    /**
     * Get method by name and descriptor.
     *
     * @param name     method name.
     * @param classes  method parameters.
     * @return a method.
     * @throws NotFoundException if the method could not be found.
     */
    CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException;

    /**
     * Get filters.
     *
     * @return filters.
     */
    Collection<Predicate<CtClass>> getFilters();

    /**
     * Get the owner method.
     *
     * @return an owner method.
     */
    Method getOwnerMethod();

    /**
     * Get the class transformer.
     *
     * @return a class transformer.
     */
    ClassTransform getClassTransform();

    /**
     * Get the owner.
     *
     * @return an owner.
     */
    Object getOwner();

    /**
     * Get the name resolver.
     *
     * @return a name resolver.
     */
    NameResolver getNameResolver();

    /**
     * Get the class.
     *
     * @return a class.
     */
    CtClass getCtClass();

    /**
     * Set the class.
     *
     * @param ctClass a class.
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
