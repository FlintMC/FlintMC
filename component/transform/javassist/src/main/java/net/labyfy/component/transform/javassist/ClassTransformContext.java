package net.labyfy.component.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Predicate;

@Deprecated
public interface ClassTransformContext {
    Class getOwnerClass();

    CtField getField(String name);

    CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers);

    CtMethod addMethod(String src);

    CtMethod getOwnerMethod(String name, String desc);

    CtField addField(Class type, String name, Modifier... modifiers);

    CtField addField(String type, String name, Modifier... modifiers);

    CtField addField(Class type, String name, String value, Modifier... modifiers);

    CtField addField(String type, String name, String value, Modifier... modifiers);

    CtMethod getDeclaredMethod(String name, Class... classes);

    Collection<Predicate<CtClass>> getFilters();

    Method getOwnerMethod();

    ClassTransform getClassTransform();

    Object getOwner();

    NameResolver getNameResolver();

    CtClass getCtClass();

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
