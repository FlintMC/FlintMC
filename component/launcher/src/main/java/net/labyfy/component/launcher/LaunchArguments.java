package net.labyfy.component.launcher;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

@Parameters
public class LaunchArguments {
  @Parameter(
      names = {"--launch-target", "-t"},
      description = "Main class to launch, defaults to net.minecraft.client.Main"
  )
  private String launchTarget = "net.minecraft.client.main.Main";

  public String getLaunchTarget() {
    return launchTarget;
  }

  @Parameter
  private List<String> otherArguments = new ArrayList<>();

  public List<String> getOtherArguments() {
    return otherArguments;
  }
}
