package net.labyfy.component.transform.launchplugin;

import net.labyfy.component.transform.launchplugin.ClassTransformException;

@FunctionalInterface
public interface LateInjectedTransformer {
  /**
   * Transform a class.
   *
   * @param className a class name.
   * @param classData a byte array reassembling a class.
   * @return a derivative of class data.
   * @throws ClassTransformException if the class transformation failed.
   */
  byte[] transform(String className, byte[] classData) throws ClassTransformException;
}
