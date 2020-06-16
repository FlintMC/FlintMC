package net.labyfy.component.gui.juklearmc.v1_15_2;

import net.janrupf.juklear.backend.JuklearBackend;
import net.janrupf.juklear.lwjgl.opengl.JuklearOpenGL;
import net.labyfy.component.gui.juklearmc.JuklearMCVersionedProvider;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Singleton;

@Singleton
@Implement(JuklearMCVersionedProvider.class)
public class LabyfyJuklearMCVersionedBackendProvider implements JuklearMCVersionedProvider {
  private JuklearOpenGL backend;

  @Override
  public JuklearBackend backend() {
    if(backend == null) {
      backend = new JuklearOpenGL();
    }

    return backend;
  }
}
