package net.labyfy.internal.component.gui.juklearmc.v1_15_2;

import com.google.inject.Singleton;
import net.janrupf.juklear.backend.JuklearBackend;
import net.labyfy.component.gui.juklearmc.JuklearMCBackendProvider;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.gui.juklearmc.v1_15_2.backend.MinecraftJuklearBackend;

/**
 * 1.15.2 Implementation of the {@link JuklearMCBackendProvider} using the LWJGL3-OpenGL backend
 * of Juklear.
 */
@Singleton
@Implement(JuklearMCBackendProvider.class)
public class VersionedJuklearMCBackendProvider implements JuklearMCBackendProvider {
  private JuklearBackend backend;

  /**
   * {@inheritDoc}
   */
  @Override
  public JuklearBackend backend() {
    if (backend == null) {
      backend = new MinecraftJuklearBackend();
    }

    return backend;
  }
}
