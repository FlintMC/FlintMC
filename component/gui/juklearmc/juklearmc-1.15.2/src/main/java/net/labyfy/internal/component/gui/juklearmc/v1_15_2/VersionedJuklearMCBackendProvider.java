package net.labyfy.internal.component.gui.juklearmc.v1_15_2;

import net.janrupf.juklear.backend.JuklearBackend;
import net.janrupf.juklear.lwjgl.opengl.JuklearOpenGL;
import net.labyfy.component.gui.juklearmc.JuklearMCBackendProvider;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Singleton;

/**
 * 1.15.2 Implementation of the {@link JuklearMCBackendProvider} using the LWJGL3-OpenGL backend
 * of Juklear.
 */
@Singleton
@Implement(JuklearMCBackendProvider.class)
public class VersionedJuklearMCBackendProvider implements JuklearMCBackendProvider {
  private JuklearOpenGL backend;

  /**
   * {@inheritDoc}
   */
  @Override
  public JuklearBackend backend() {
    if(backend == null) {
      backend = new JuklearOpenGL();
    }

    return backend;
  }
}
