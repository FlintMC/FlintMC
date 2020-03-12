package net.labyfy.component.transform.tweaker;

import com.google.common.base.Preconditions;
import com.google.common.reflect.ClassPath;
import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.util.Map;

public class LabyEntryPoint {
  private static boolean launched = false;
  private final Map<String, String> launchArguments;

  private LabyEntryPoint(Map<String, String> launchArguments) {
    this.launchArguments = launchArguments;
  }

  private void launch() {
    System.out.println("Launch in Launchclassloader " + getClass().getClassLoader());
  }

  public static void launch(Map<String, String> launchArguments) throws IOException {
    if (launched) return;
    Preconditions.checkNotNull(launchArguments);
    launched = true;
    new LabyEntryPoint(launchArguments).launch();
  }
}
