package net.labyfy.v1_15_2.webgui.ultralight;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;

/**
 * Interop for injecting Ultralight render code.
 */
@Singleton
public class RenderHook {
  private final UltralightWebGuiController controller;

  @Inject
  private RenderHook(UltralightWebGuiController controller) {
    this.controller = controller;
  }

  @Hook(
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "updateCameraAndRender",
      parameters = {
        @Type(reference = float.class),
        @Type(reference = long.class),
        @Type(reference = boolean.class)
      })
  public void renderHook() {
    controller.renderAll();
  }
}
