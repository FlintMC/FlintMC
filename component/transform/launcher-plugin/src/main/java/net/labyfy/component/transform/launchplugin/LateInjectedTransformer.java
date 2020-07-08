package net.labyfy.component.transform.launchplugin;

import net.labyfy.component.transform.exceptions.ClassTransformException;

@FunctionalInterface
public interface LateInjectedTransformer {
  /**
   * Transform a class.
   *
   * @param className  A class name.
   * @param classData  A byte array reassembling a class.
   * @return A derivative of class data.
   * @throws ClassTransformException If the class transformation failed.
   */
  byte[] transform(String className, byte[] classData) throws ClassTransformException;
}
