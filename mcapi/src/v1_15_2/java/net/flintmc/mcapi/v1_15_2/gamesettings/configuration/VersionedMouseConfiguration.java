package net.flintmc.mcapi.v1_15_2.gamesettings.configuration;

import com.google.inject.Singleton;
import net.flintmc.mcapi.gamesettings.configuration.MouseConfiguration;
import net.flintmc.framework.inject.implement.Implement;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of the {@link MouseConfiguration}.
 */
@Singleton
@Implement(value = MouseConfiguration.class, version = "1.15.2")
public class VersionedMouseConfiguration implements MouseConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMouseSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseSensitivity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMouseSensitivity(double mouseSensitivity) {
    Minecraft.getInstance().gameSettings.mouseSensitivity = mouseSensitivity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMouseWheelSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseWheelSensitivity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMouseWheelSensitivity(double mouseWheelSensitivity) {
    Minecraft.getInstance().gameSettings.mouseWheelSensitivity = mouseWheelSensitivity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRawMouseInput() {
    return Minecraft.getInstance().gameSettings.rawMouseInput;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRawMouseInput(boolean rawMouseInput) {
    Minecraft.getInstance().gameSettings.rawMouseInput = rawMouseInput;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvertMouse() {
    return Minecraft.getInstance().gameSettings.invertMouse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInvertMouse(boolean invertMouse) {
    Minecraft.getInstance().gameSettings.invertMouse = invertMouse;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDiscreteMouseScroll() {
    return Minecraft.getInstance().gameSettings.discreteMouseScroll;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDiscreteMouseScroll(boolean discreteMouseScroll) {
    Minecraft.getInstance().gameSettings.discreteMouseScroll = discreteMouseScroll;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTouchscreen() {
    return Minecraft.getInstance().gameSettings.touchscreen;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTouchscreen(boolean touchscreen) {
    Minecraft.getInstance().gameSettings.touchscreen = touchscreen;
    Minecraft.getInstance().gameSettings.saveOptions();
  }


}
