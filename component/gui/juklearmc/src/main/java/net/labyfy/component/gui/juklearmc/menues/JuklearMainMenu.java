package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.image.JuklearImageConvert;
import net.janrupf.juklear.image.JuklearImageException;
import net.janrupf.juklear.image.JuklearJavaImage;
import net.janrupf.juklear.layout.JuklearPanelFlags;
import net.janrupf.juklear.layout.component.JuklearButton;
import net.janrupf.juklear.layout.component.JuklearImageDisplay;
import net.janrupf.juklear.layout.component.JuklearSpacing;
import net.janrupf.juklear.layout.component.JuklearWindow;
import net.janrupf.juklear.layout.component.base.JuklearTopLevelComponent;
import net.janrupf.juklear.layout.component.row.JuklearDynamicRow;
import net.janrupf.juklear.layout.component.row.JuklearStaticRow;
import net.janrupf.juklear.layout.component.row.template.JuklearTemplatedRow;
import net.janrupf.juklear.style.JuklearStyle;
import net.labyfy.component.gui.juklearmc.JuklearMC;
import net.labyfy.component.gui.juklearmc.JuklearScreen;
import net.labyfy.component.gui.screen.BuiltinScreenDisplayer;
import net.labyfy.component.gui.screen.ScreenName;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Collections;

import static org.lwjgl.opengl.GL20.*;

@Singleton
@JuklearScreen(ScreenName.MAIN_MENU)
public class JuklearMainMenu implements JuklearMCScreen {
  private static final int WINDOW_WIDTH = 530;
  private static final int WINDOW_HEIGHT = 490;

  private final JuklearWindow mainWindow;

  private int width;
  private int height;
  private int backgroundWidth;
  private int backgroundHeight;

  private int backgroundTexture;

  @Inject
  private JuklearMainMenu(JuklearMC juklearMC, BuiltinScreenDisplayer displayer) {
    this.mainWindow = new JuklearWindow("Test");
    createBackground();

    JuklearContext context = juklearMC.getContext();

    try {
      JuklearJavaImage logo = JuklearImageConvert.fromBufferedImage(
          juklearMC.getContext().getJuklear(),
          ImageIO.read(getClass().getResource("/assets/labymod/textures/gui/logo.png"))
      );
      JuklearImageDisplay logoDisplay = new JuklearImageDisplay(logo);

      JuklearTemplatedRow logoRow = new JuklearTemplatedRow(logo.getHeight() - 5);
      logoRow.addDynamic(new JuklearSpacing(1));
      logoRow.addStatic(logo.getWidth(), logoDisplay);
      logoRow.addDynamic(new JuklearSpacing(0));

      mainWindow.addChild(logoRow);
      mainWindow.addChild(new JuklearStaticRow(0, 75));
    } catch (IOException | JuklearImageException e) {
      e.printStackTrace();
    }

    JuklearDynamicRow singlePlayerRow = new JuklearDynamicRow(50);
    JuklearButton singleplayerButton = new JuklearButton("Singleplayer");
    singleplayerButton.addListener(context, (e) -> displayer.display(ScreenName.minecraft(ScreenName.SINGLEPLAYER)));
    singlePlayerRow.addChild(singleplayerButton);
    mainWindow.addChild(singlePlayerRow);

    mainWindow.addChild(new JuklearDynamicRow(1));

    JuklearDynamicRow multiplayerRow = new JuklearDynamicRow(50);

    JuklearButton multiplayerButton = new JuklearButton("Multiplayer");
    multiplayerButton.addListener(context, (e) -> displayer.display(ScreenName.minecraft(ScreenName.MULTIPLAYER)));
    multiplayerRow.addChild(multiplayerButton);
    mainWindow.addChild(multiplayerRow);

    mainWindow.addChild(new JuklearDynamicRow(12f));

    JuklearTemplatedRow settingsRow = new JuklearTemplatedRow(50);
    settingsRow.addChildStyle(context.getStyle().getButton().getRounding().preparePush(6f));
    settingsRow.addChildStyle(context.getStyle().getButton().getNormal().preparePush(6, 16, 23, 56));
    JuklearButton optionsButton = new JuklearButton("Options");
    optionsButton.addListener(context, (e) -> displayer.display(ScreenName.minecraft(ScreenName.OPTIONS)));
    settingsRow.addDynamic(optionsButton);
    settingsRow.addStatic(7, new JuklearSpacing(1));
    settingsRow.addDynamic(new JuklearButton("LabyMod Settings"));
    mainWindow.addChild(settingsRow);

    JuklearStyle style = juklearMC.getContext().getStyle();
    mainWindow.addOwnStyle(style.getWindow().getFixedBackground().preparePush(0, 0, 0, 0));
    mainWindow.addFlag(JuklearPanelFlags.NO_SCROLLBAR);
  }

  @Override
  public Collection<JuklearTopLevelComponent> topLevelComponents() {
    return Collections.singleton(mainWindow);
  }

  @Override
  public void updateSize(int width, int height) {
    int left = (width / 2) - (WINDOW_WIDTH / 2);
    int top = ((height / 2) - (WINDOW_HEIGHT / 2)) - 50;

    mainWindow.setBounds(left, top, WINDOW_WIDTH, WINDOW_HEIGHT);

    this.width = width;
    this.height = height;
  }

  private void createBackground() {
    glEnable(GL_TEXTURE_2D);
    backgroundTexture = glGenTextures();

    BufferedImage backgroundImage;
    try {
      backgroundImage = ImageIO.read(getClass().getResource("/assets/labymod/textures/gui/background.png"));
    } catch (IOException e) {
      glDeleteTextures(backgroundTexture);
      backgroundTexture = -2;
      e.printStackTrace();
      return;
    }

    backgroundWidth = backgroundImage.getWidth();
    backgroundHeight = backgroundImage.getHeight();

    int[] pixels = new int[backgroundWidth * backgroundHeight];
    backgroundImage.getRGB(
        0,
        0,
        backgroundWidth,
        backgroundHeight,
        pixels,
        0,
        backgroundWidth
    );

    ByteBuffer data = ByteBuffer.allocateDirect(pixels.length * Integer.BYTES);
    JuklearImageConvert.convertARGBtoRGBA(IntBuffer.wrap(pixels), data.asIntBuffer());

    glBindTexture(GL_TEXTURE_2D, backgroundTexture);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGBA8,
        backgroundWidth,
        backgroundHeight,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        data
    );
    glDisable(GL_TEXTURE_2D);
  }

  @Override
  public void preNuklearRender() {
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, backgroundTexture);

    glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    glOrtho(0, width, height, 0, -1, 1);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();

    glLoadIdentity();
    glDisable(GL_LIGHTING);
    glDisable(GL_SCISSOR_TEST);
    glEnable(GL_BLEND);
    glEnable(GL_TEXTURE_2D);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glColor3f(1, 1, 1);

    glBegin(GL_QUADS);

    glTexCoord2i(0, 0);
    glVertex2f(0, 0);

    float widthRatio = ((float) width) / ((float) backgroundWidth);
    float heightRatio = ((float) height) / ((float) backgroundHeight);

    glTexCoord2f(0, heightRatio);
    glVertex2i(0, height);

    glTexCoord2f(widthRatio, heightRatio);
    glVertex2i(width, height);

    glTexCoord2f(widthRatio, 0);
    glVertex2i(width, 0);

    glEnd();

    glPopMatrix();
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glMatrixMode(GL_MODELVIEW);

    glDisable(GL_TEXTURE_2D);
    glPopAttrib();
  }
}
