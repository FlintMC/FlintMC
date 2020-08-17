package net.labyfy.component.render;

/**
 * Provider to obtain a {@link VertexBuffer} from
 */
public interface VertexBufferProvider {

  /**
   * @param renderType the render context
   * @return a vertex buffer to render vertices to
   */
  VertexBuffer get(RenderType renderType);

}
