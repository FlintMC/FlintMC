package net.labyfy.component.transform.tweaker;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LabyTransformer implements IClassTransformer {

  public byte[] transform(String s, String s1, byte[] bytes) {
    bytes = this.addServiceNotifier(bytes);
    return bytes;
  }

  private byte[] addServiceNotifier(byte[] bytes) {

    try {
      CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(bytes));
      CtConstructor classInitializer = ctClass.getClassInitializer();
      if (classInitializer == null) classInitializer = ctClass.makeClassInitializer();

      classInitializer.insertAfter(
          "net.labyfy.component.transform.tweaker.LabyTransformer.notifyService("
              + ctClass.getName()
              + ".class);");
      return ctClass.toBytecode();
    } catch (IOException | CannotCompileException e) {
      e.printStackTrace();
    }

    return bytes;
  }

  public static void notifyService(Class clazz) {
    try {
      Launch.classLoader
          .loadClass("net.labyfy.component.initializer.EntryPoint")
          .getDeclaredMethod("notifyService", Class.class)
          .invoke(null, clazz);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
