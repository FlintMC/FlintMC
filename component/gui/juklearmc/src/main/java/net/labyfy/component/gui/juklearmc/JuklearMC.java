package net.labyfy.component.gui.juklearmc;

import net.janrupf.juklear.Juklear;
import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.drawing.JuklearAntialiasing;
import net.janrupf.juklear.exception.JuklearInitializationException;
import net.janrupf.juklear.font.JuklearFont;
import net.janrupf.juklear.font.JuklearFontAtlas;
import net.janrupf.juklear.font.JuklearFontAtlasEditor;
import net.janrupf.juklear.math.JuklearVec2;
import net.janrupf.juklear.util.JuklearNatives;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
@Task(value = Tasks.POST_OPEN_GL_INITIALIZE, async = false)
public class JuklearMC {
  private final Juklear juklear;

  private JuklearContext context;
  private JuklearFont defaultFont;

  @Inject
  private JuklearMC(JuklearMCVersionedProvider versionedProvider) throws IOException {
    JuklearNatives.setupWithTemporaryFolder();
    juklear = Juklear.usingInternalGarbageCollection(versionedProvider.backend());
  }

  @TaskBody
  public void initialize() throws JuklearInitializationException {
    juklear.init();

    JuklearFontAtlas fontAtlas = juklear.defaultFontAtlas();
    JuklearFontAtlasEditor editor = fontAtlas.begin();
    defaultFont = editor.addDefault(13);
    editor.end();

    context = juklear.defaultContext(defaultFont);
  }

  public void draw(int width, int height) {
    context.draw(width, height, new JuklearVec2(juklear, 1, 1), JuklearAntialiasing.ON);
  }


  public JuklearContext getContext() {
    return context;
  }
}
