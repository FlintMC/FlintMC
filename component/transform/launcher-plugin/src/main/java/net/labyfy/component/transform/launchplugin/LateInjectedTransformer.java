package net.labyfy.component.transform.launchplugin;

import net.labyfy.component.transform.exceptions.ClassTransformException;

@FunctionalInterface
public interface LateInjectedTransformer {
  byte[] transform(String className, byte[] classData) throws ClassTransformException;
}
