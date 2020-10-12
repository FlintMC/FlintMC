package net.labyfy.component.render.vbo;

/**
 * Builder to create new vertex formats. This specification does not require support for
 * concurrency, so the implementation might only allow for one format to be build at a time (might
 * be implemented as naive singleton).
 */
public interface VertexFormatBuilder {

  /**
   * Adds a generic vertex attribute to the format.
   *
   * @param attribute the {@link VertexAttribute} to be added.
   * @return the instance.
   */
  VertexFormatBuilder addAttribute(VertexAttribute attribute);

  /**
   * @return creates a new {@link VertexFormat} based on the generic vertex attributes added since
   *     the last build call and resets the list of generic vertex attributes.
   */
  VertexFormat build();
}
