package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;

/**
 * Represents a box in the 3 dimensional coordinate space.
 * Utility class to render boxes more easily and match the minecraft structure better.
 */
public interface VertexBox {

  /**
   * @return the back side of the box
   */
  VertexQuad getBack();

  /**
   * @return the bottom side of the box
   */
  VertexQuad getBottom();

  /**
   * @return the front side of the box
   */
  VertexQuad getFront();

  /**
   * @return the left side of the box
   */
  VertexQuad getLeft();

  /**
   * @return the right side of the box
   */
  VertexQuad getRight();

  /**
   * @return the top side of the box
   */
  VertexQuad getTop();

  /**
   * Renders this box to a given 3d context
   *
   * @param matrixStack  the world context to render into
   * @param vertexBuffer the vertex data to render into
   * @return this
   */
  VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  @AssistedFactory(VertexBox.class)
  interface Factory {
    VertexBox create(
        @Assisted("dimensions") Vector3f dimensions,
        @Assisted("textureOffset") Vector2f textureOffset,
        @Assisted("textureDensity") Vector2f textureDensity,
        @Assisted("color") Color color,
        @Assisted("lightMap") int lightMap
    );
  }

}
