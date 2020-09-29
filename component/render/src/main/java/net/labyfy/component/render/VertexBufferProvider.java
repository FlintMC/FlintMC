package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Matrix4f;

import java.util.Collection;

/**
 * Provider to obtain a {@link VertexBuffer} from
 */
public interface VertexBufferProvider {

  /**
   * @param renderType the render context
   * @return a vertex buffer to render vertices to
   */
  VertexBuffer get(RenderType renderType);

  VertexBufferProvider setMatrixTransform(Matrix4f matrix4f);

  Collection<VertexBuffer> getCreatedBuffers();

  @AssistedFactory(VertexBufferProvider.class)
  interface Factory {
    VertexBufferProvider create(@Assisted("matrixStack") Object matrixStack, @Assisted("renderTypeBuffer") Object renderTypeBuffer);
  }

}
