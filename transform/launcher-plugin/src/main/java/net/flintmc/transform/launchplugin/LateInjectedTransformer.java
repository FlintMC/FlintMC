package net.flintmc.transform.launchplugin;

import net.flintmc.transform.exceptions.ClassTransformException;

@FunctionalInterface
public interface LateInjectedTransformer {
  /**
   * Transform a class.
   *
   * @param className A class name.
   * @param classData A byte array reassembling a class.
   * @return A derivative of class data.
   * @throws ClassTransformException If the class transformation failed.
   */
  byte[] transform(String className, byte[] classData) throws ClassTransformException;
}
