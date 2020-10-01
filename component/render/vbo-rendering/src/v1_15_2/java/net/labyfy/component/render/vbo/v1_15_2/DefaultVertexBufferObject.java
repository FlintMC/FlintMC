package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexBufferObject;
import net.labyfy.component.render.vbo.VertexBuilder;
import net.labyfy.component.render.vbo.VertexFormat;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexBufferObject.class)
public class DefaultVertexBufferObject implements VertexBufferObject {

    private final VertexFormat vertexFormat;
    private final VertexBuilder.Factory vertexBuilderFactory;

    private final int id;
    private List<VertexBuilder> vertices;

    @AssistedInject
    private DefaultVertexBufferObject(@Assisted VertexFormat vertexFormat, VertexBuilder.Factory vertexBuilderFactory) {
        this.vertexFormat = vertexFormat;
        this.vertexBuilderFactory = vertexBuilderFactory;
        this.vertices = new ArrayList<>();
        this.id = glGenBuffers();
    }
    
    @Override
    public VertexBuilder addVertex() {
        return this.vertexBuilderFactory.create(this);
    }

    @Override
    public void addVertex(VertexBuilder vertexBuilder) {
        this.vertices.add(vertexBuilder);
    }

    @Override
    public void pushToGPU() {

        int totalSize = vertices.size() * vertexFormat.getVertexSize();



        this.vertices = null;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void bind() {

    }

    @Override
    public void unbind() {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
