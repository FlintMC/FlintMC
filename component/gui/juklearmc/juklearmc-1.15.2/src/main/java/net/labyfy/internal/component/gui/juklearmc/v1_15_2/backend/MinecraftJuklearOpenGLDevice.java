package net.labyfy.internal.component.gui.juklearmc.v1_15_2.backend;

import net.janrupf.juklear.Juklear;
import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.drawing.*;
import net.janrupf.juklear.ffi.CAccessibleObject;
import net.janrupf.juklear.ffi.CAllocatedObject;
import net.janrupf.juklear.image.JuklearImageFormat;
import net.janrupf.juklear.image.JuklearJavaImage;
import net.janrupf.juklear.math.JuklearVec2;
import net.janrupf.juklear.util.JuklearBuffer;
import net.janrupf.juklear.util.JuklearConstants;
import net.janrupf.juklear.util.JuklearConvertResult;
import net.labyfy.internal.component.gui.juklearmc.v1_15_2.exception.MinecraftJuklearOpenGLFatalException;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.opengl.GL20.*;

public class MinecraftJuklearOpenGLDevice {
  private final Juklear juklear;
  private final JuklearBuffer commandBuffer;
  private final Queue<Runnable> preFrameTasks;

  private JuklearDrawNullTexture nullTexture;

  public MinecraftJuklearOpenGLDevice(Juklear juklear) {
    this.juklear = juklear;
    this.commandBuffer = juklear.defaultBuffer();
    this.preFrameTasks = new LinkedList<>();
  }

  public Queue<Runnable> getPreFrameTasks() {
    return preFrameTasks;
  }

  public void draw(
      JuklearContext context, int width, int height, JuklearVec2 scale, JuklearAntialiasing antialiasing) {
    preFrameTasks.forEach(Runnable::run);

    glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);
    glDisable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_SCISSOR_TEST);
    glEnable(GL_BLEND);
    glEnable(GL_TEXTURE_2D);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glViewport(0, 0, width, height);
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();

    glOrtho(
        0.0,
        width * scale.getX(),
        height * scale.getY(),
        0.0,
        -1.0,
        1.0);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();
    glLoadIdentity();

    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);

    // TODO: Move out of here
    JuklearConvertConfig convertConfig = juklear.convertConfig()
        .addVertexLayout(juklear.drawVertexLayoutElement()
            .attribute(JuklearDrawVertexLayoutAttribute.POSITION)
            .format(JuklearDrawVertexLayoutFormat.FLOAT)
            .offset(MinecraftJuklearOpenGLVertex.positionOffset())
            .build())
        .addVertexLayout(juklear.drawVertexLayoutElement()
            .attribute(JuklearDrawVertexLayoutAttribute.TEXCOORD)
            .format(JuklearDrawVertexLayoutFormat.FLOAT)
            .offset(MinecraftJuklearOpenGLVertex.uvOffset())
            .build())
        .addVertexLayout(juklear.drawVertexLayoutElement()
            .attribute(JuklearDrawVertexLayoutAttribute.COLOR)
            .format(JuklearDrawVertexLayoutFormat.R8G8B8A8)
            .offset(MinecraftJuklearOpenGLVertex.colorOffset())
            .build())
        .vertexSize(MinecraftJuklearOpenGLVertex.byteSize())
        .nullTexture(nullTexture)
        .circleSegmentCount(1000)
        .curveSegmentCount(1000)
        .arcSegmentCount(1000)
        .globalAlpha(1.0f)
        .shapeAA(antialiasing)
        .lineAA(antialiasing)
        .build();

    JuklearBuffer vertexBuffer = juklear.defaultBuffer();
    JuklearBuffer elementBuffer = juklear.defaultBuffer();

    JuklearConvertResult result = context.convert(commandBuffer, vertexBuffer, elementBuffer, convertConfig);
    if (result != JuklearConvertResult.SUCCESS) {
      throw new MinecraftJuklearOpenGLFatalException("Failed to convert draw data to OpenGL: " + result.name());
    }

    ByteBuffer constVertexBuffer = vertexBuffer.constMemory();
    glVertexPointer(2, GL_FLOAT, MinecraftJuklearOpenGLVertex.byteSize(),
        (ByteBuffer) constVertexBuffer.slice().position(MinecraftJuklearOpenGLVertex.positionOffset()));
    glTexCoordPointer(2, GL_FLOAT, MinecraftJuklearOpenGLVertex.byteSize(),
        (ByteBuffer) constVertexBuffer.slice().position(MinecraftJuklearOpenGLVertex.uvOffset()));
    glColorPointer(4, GL_UNSIGNED_BYTE, MinecraftJuklearOpenGLVertex.byteSize(),
        (ByteBuffer) constVertexBuffer.slice().position(MinecraftJuklearOpenGLVertex.colorOffset()));

    ByteBuffer constElementBuffer = elementBuffer.constMemory();
    context.drawForEach(commandBuffer, (drawCommand) -> {
      if (drawCommand.getElementCount() < 1) {
        return;
      }

      JuklearJavaImage image = JuklearJavaImage.wrapExisting(
          juklear,
          drawCommand.getTexture().getHandle()
      );

      glBindTexture(GL_TEXTURE_2D, (int) image.getBackendObject().getHandle());
            /* glScissor(
                    (int) (drawCommand.getClipRect().getX() * scale.getX()) - 5,
                    (int) ((height -
                            (drawCommand.getClipRect().getY() + drawCommand.getClipRect().getHeight())) * scale.getY()),
                    (int) (drawCommand.getClipRect().getWidth() * scale.getX()) + 10,
                    (int) (drawCommand.getClipRect().getHeight() * scale.getY())
            ); */

      long memAddr = MemoryUtil.memAddress(constElementBuffer);
      if (memAddr == 0) {
        throw new IllegalStateException();
      }

      glDrawElements(GL_TRIANGLES, (int) drawCommand.getElementCount(),
          GL_UNSIGNED_SHORT, memAddr);

      constElementBuffer.position(
          (int) (constElementBuffer.position() +
              (JuklearConstants.DRAW_INDEX_SIZE * drawCommand.getElementCount())));
    });

    commandBuffer.clear();

    glDisableClientState(GL_VERTEX_ARRAY);
    glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    glDisableClientState(GL_COLOR_ARRAY);

    glDisable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_SCISSOR_TEST);
    glDisable(GL_BLEND);
    glDisable(GL_TEXTURE_2D);

    glBindTexture(GL_TEXTURE_2D, 0);
    glMatrixMode(GL_MODELVIEW);
    glPopMatrix();
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glPopAttrib();
  }

  public JuklearJavaImage uploadFontAtlas(CAccessibleObject<?> image, int width, int height) {
    int fontTexture = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, fontTexture);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
        0, GL_RGBA, GL_UNSIGNED_BYTE, image.getHandle());

    return JuklearJavaImage.forFontAtlas(
        juklear,
        CAllocatedObject.of(fontTexture).withoutFree(),
        width,
        height
    );
  }

  public int uploadTexture(JuklearImageFormat format, ByteBuffer data, int width, int height) {
    int id = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, id);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);

    switch (format) {
      case UNSIGNED_BYTE_RGBA:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height,
            0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        break;

      default:
        glDeleteTextures(id);
        throw new UnsupportedOperationException(
            "The OpenGL2 backend does not support the image format " + format.name());
    }

    return id;
  }

  public void setNullTexture(JuklearDrawNullTexture nullTexture) {
    this.nullTexture = nullTexture;
  }
}
