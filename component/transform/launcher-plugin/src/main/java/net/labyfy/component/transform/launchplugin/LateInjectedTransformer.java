package net.labyfy.component.transform.launchplugin;

@FunctionalInterface
public interface LateInjectedTransformer {
  byte[] transform(String className, byte[] classData);
}
