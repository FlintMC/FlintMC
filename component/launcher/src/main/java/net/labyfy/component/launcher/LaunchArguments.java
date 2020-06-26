package net.labyfy.component.launcher;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * POJ for JCommander
 */
@Parameters
public class LaunchArguments {
    @Parameter(
            names = {"--launch-target", "-t"},
            description = "Main class to launch, defaults to net.minecraft.client.Main"
    )
    private final String launchTarget = "net.minecraft.client.main.Main";

  // Collect all other arguments so we can pass them on to minecraft
    @Parameter
    private final List<String> otherArguments = new ArrayList<>();

    /**
     * Retrieves the launch target which control should be handed over to after plugins
     * have hooked the launch process.
     *
     * @return The launch target which control should be handed over to
     */
    public String getLaunchTarget() {
        return launchTarget;
    }

  /**
   * Retrieves a list of arguments not processed by any commandline handler
   *
   * @return All unhandled arguments
   */
  public List<String> getOtherArguments() {
    return otherArguments;
  }
}
