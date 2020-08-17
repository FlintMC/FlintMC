package net.labyfy.component.render;

/**
 * The batched rendering that minecraft 1.15.2 introduces every rendered vertex will use render states to be described and rendered.
 * For example, one render state could be to enable a certain texture at {@link RenderState#enable()} and disable it at {@link RenderState#disable()}.
 * <p>
 * The render states for the current context are defined in {@link RenderType#getCustomStates()} in addition to all predefined states. ({@link RenderType#alpha(float)} {@link RenderType#depthTest(int)} etc)
 * Before a vertex will be rendered through the batched rendering system, {@link RenderState#enable()} will be called to enable all states.
 * When the render is finished the state will {@link RenderState#disable()} itself to clean up the opengl state.
 * <p>
 * So it is important to always disable everything after the render that was enabled before the render.
 */
public interface RenderState {

  /**
   * Enables the render state.
   */
  void enable();

  /**
   * Disables the render state.
   */
  void disable();

  /**
   * @return the name of the render state
   */
  String getName();
}
