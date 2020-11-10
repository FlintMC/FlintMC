package net.flintmc.framework.stereotype.service;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.launcher.LaunchController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CtResolver {

  private static final Map<CtClass, Class<?>> classes = new HashMap<>();
  private static final Map<Integer, Method> methods = new HashMap<>();
  private static final Map<Integer, Constructor<?>> constructors = new HashMap<>();

  public static Constructor<?> get(CtConstructor ctConstructor) {
    int hash = Objects.hash(ctConstructor, ctConstructor.getDeclaringClass());
    if (!constructors.containsKey(hash)) {
      try {
        Class<?>[] parameters = new Class[ctConstructor.getParameterTypes().length];
        for (int i = 0; i < ctConstructor.getParameterTypes().length; i++) {
          parameters[i] = Class.forName(ctConstructor.getParameterTypes()[i].getName());
        }
        Constructor<?> declaredConstructor =
                LaunchController.getInstance()
                        .getRootLoader()
                        .loadClass(ctConstructor.getDeclaringClass().getName())
                        .getDeclaredConstructor(parameters);
        declaredConstructor.setAccessible(true);
        constructors.put(hash, declaredConstructor);
      } catch (ClassNotFoundException | NoSuchMethodException | NotFoundException e) {
        e.printStackTrace();
      }
    }
    return constructors.get(hash);
  }

  /**
   * @param ctMethod the {@link CtMethod} to find the reflect representation from
   * @return the reflect representation of ctMethod
   */
  public static Method get(CtMethod ctMethod) {
    int hash = hash(ctMethod);
    if (!methods.containsKey(hash)) {
      try {
        Class<?>[] parameters = new Class[ctMethod.getParameterTypes().length];
        for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
          parameters[i] = Class.forName(ctMethod.getParameterTypes()[i].getName());
        }
        Method declaredMethod =
            LaunchController.getInstance()
                .getRootLoader()
                .loadClass(ctMethod.getDeclaringClass().getName())
                .getDeclaredMethod(ctMethod.getName(), parameters);
        declaredMethod.setAccessible(true);
        methods.put(hash, declaredMethod);
      } catch (ClassNotFoundException | NoSuchMethodException | NotFoundException e) {
        e.printStackTrace();
      }
    }
    return methods.get(hash);
  }

  private static int hash(CtMethod ctMethod) {
    return Objects.hash(ctMethod, ctMethod.getDeclaringClass());
  }

  /**
   * @param ctClass the {@link CtClass} to find the reflect representation from
   * @return the reflect representation of ctClass
   */
  public static <T> Class<T> get(CtClass ctClass) {
    if (!classes.containsKey(ctClass)) {
      try {
        classes.put(
            ctClass, LaunchController.getInstance().getRootLoader().loadClass(ctClass.getName()));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return (Class<T>) classes.get(ctClass);
  }
}
