package net.labyfy.component.gui.mcjfxgl.component.labeled.button;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.old.component.GuiComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePackProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@IgnoreInitialization
public class MinecraftButtonSkin extends SkinBase<Button.Handle> implements GuiComponent {

  private final ObservableSet<PseudoClass> states;
  private final ImageView backgroundLeft;
  private final ImageView backgroundRight;
  private final Button component;
  private final ResourceLocationProvider resourceLocationProvider;
  private final ResourcePackProvider resourcePackProvider;
  private final BufferedImage widgets;
  private final InvalidationListener invalidationListener;
  private final SetChangeListener<PseudoClass> pseudoClassListener;

  @AssistedInject
  private MinecraftButtonSkin(
      @Assisted Button component,
      ResourceLocationProvider resourceLocationProvider,
      ResourcePackProvider resourcePackProvider)
      throws IOException {
    super((Button.Handle) component.getControl());
    this.component = component;
    this.resourceLocationProvider = resourceLocationProvider;
    this.resourcePackProvider = resourcePackProvider;
    this.getChildren().clear();

    this.widgets =
        ImageIO.read(resourceLocationProvider.get("textures/gui/widgets.png").openInputStream());
    this.backgroundLeft = this.createBackground();
    this.backgroundRight = this.createBackground();

    this.states = this.getSkinnable().getPseudoClassStates();

    this.invalidationListener = observable->updateState();

    this.getSkinnable().widthProperty().addListener(invalidationListener);
    this.getSkinnable().heightProperty().addListener(invalidationListener);

    this.pseudoClassListener = change -> {
          if (change.wasAdded()
              && change.getElementAdded().getPseudoClassName().equals("hover")) {
            this.updateState();
          } else if (change.wasRemoved()
              && change.getElementRemoved().getPseudoClassName().equals("hover")) {
            this.updateState();
          }
        };
    this.states.addListener(this.pseudoClassListener );

    //    this.updateState();

    Text text = new Text();


    text.setTranslateY(5);
    text.textProperty().bindBidirectional(component.textProperty());
    text.fontProperty().bindBidirectional(component.textFontProperty());
    text.fillProperty().bindBidirectional(component.textFillProperty());
    text.setFont(
        Font.loadFont(
            getClass()
                .getClassLoader()
                .getResourceAsStream("assets/minecraft/fonts/Minecraftia-Regular.ttf"),
            32));

    text.setEffect(
        new DropShadow(
            BlurType.ONE_PASS_BOX, new Color(63f / 255f, 63f / 255f, 63f / 255f, 1), 1, 0, 4, 4));
    this.getChildren().addAll(this.backgroundLeft, this.backgroundRight, text);
    this.getSkinnable().setStyle("-fx-text-fill: white");
  }

  private ImageView createBackground() {
    ImageView imageView = new ImageView();
    imageView.setSmooth(false);
    return imageView;
  }

  public void dispose() {
    this.states.removeListener(this.pseudoClassListener);
    this.getSkinnable().widthProperty().removeListener(this.invalidationListener);
    this.getSkinnable().heightProperty().removeListener(this.invalidationListener);
    super.dispose();
  }

  private void updateState() {
    backgroundLeft.setTranslateX(-this.getSkinnable().getWidth() / 4d);
    backgroundRight.setTranslateX(this.getSkinnable().getWidth() / 4d);
    boolean hover = false;
    for (PseudoClass state : this.states) {
      if (state.getPseudoClassName().equals("hover")) hover = true;
    }

    int leftOffsetX = 0;
    int rightOffsetX =
        (int)
            (Math.round(this.widgets.getWidth() * 200d / 256d)
                - (this.widgets.getWidth() * this.getSkinnable().getWidth() / 2d / 4d / 256d));
    int offsetY;
    int halfWidth =
        (int) Math.round(this.widgets.getWidth() * this.getSkinnable().getWidth() / 2d / 4d / 256d);
    int height =
        (int) Math.round(this.widgets.getHeight() * this.getSkinnable().getHeight() / 4d / 256d);

    int scaleFactor =
        (int) (256d / Math.min(this.widgets.getHeight(), this.widgets.getWidth()) * 4d);

    if (hover) {
      offsetY = (int) Math.round(this.widgets.getHeight() * 86d / 256d);
    } else {
      offsetY = (int) Math.round(this.widgets.getHeight() * 66d / 256d);
    }

    if (height == 0) height = 1;
    if (halfWidth == 0) halfWidth = 1;

    backgroundLeft.setImage(
        resample(
            SwingFXUtils.toFXImage(
                this.widgets.getSubimage(leftOffsetX, offsetY, halfWidth, height), null),
            scaleFactor));

    backgroundRight.setImage(
        resample(
            SwingFXUtils.toFXImage(
                this.widgets.getSubimage(rightOffsetX, offsetY, halfWidth, height), null),
            scaleFactor));
  }

  @AssistedFactory(MinecraftButtonSkin.class)
  public interface Factory {
    MinecraftButtonSkin create(Button component);
  }

  private Image resample(Image input, int scaleFactor) {

    WritableImage output =
        new WritableImage(
            (int) input.getWidth() * scaleFactor, (int) input.getHeight() * scaleFactor);

    PixelReader reader = input.getPixelReader();
    PixelWriter writer = output.getPixelWriter();

    for (int y = 0; y < input.getHeight(); y++) {
      for (int x = 0; x < input.getWidth(); x++) {
        final int argb = reader.getArgb(x, y);
        for (int dy = 0; dy < scaleFactor; dy++) {
          for (int dx = 0; dx < scaleFactor; dx++) {
            writer.setArgb(x * scaleFactor + dx, y * scaleFactor + dy, argb);
          }
        }
      }
    }
    return output;
  }
}
