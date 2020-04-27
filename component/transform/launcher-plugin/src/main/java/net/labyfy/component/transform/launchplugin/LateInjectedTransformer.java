package net.labyfy.component.transform.launchplugin;

public interface LateInjectedTransformer {
  byte[] transform(String className, byte[] classData);
}
