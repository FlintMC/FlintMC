package net.labyfy.component.launcher;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Parameters
public class LaunchArguments {
  @Parameter(names = {"--assets", "-a"}, description = "Game assets directory", converter = FileConverter.class)
  private File assetsDir;

  public File getAssetsDir() {
    return assetsDir;
  }

  @Parameter(
      names = {"--launch-target", "-t"},
      description = "Main class to launch, defaults to net.minecraft.client.Main"
  )
  private String launchTarget = "net.minecraft.client.Main";

  public String getLaunchTarget() {
    return launchTarget;
  }

  @Parameter(
      names = {"--game-version", "--version"},
      description = "Version of the launched game",
      required = true
  )
  private String gameVersion;

  @Parameter
  private final List<String> otherArguments = new ArrayList<>();

  public List<String> getOtherArguments() {
    return otherArguments;
  }
}
