package net.labyfy.component.render.vbo;

public interface VertexFormatBuilder {

    VertexFormatBuilder addAttribute(VertexAttribute attribute);

    VertexFormat build();

}
