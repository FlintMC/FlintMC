package net.labyfy.component.gui.mcjfxgl.component.labeled.button;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePackProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@IgnoreInitialization
public class DefaultButtonSkin extends SkinBase<Button.Handle> {


  @AssistedInject
  private DefaultButtonSkin(
      @Assisted Button component, ResourceLocationProvider resourceLocationProvider, ResourcePackProvider resourcePackProvider) throws IOException {
    super((Button.Handle) component.getControl());

    this.getChildren().clear();
    Text text = new Text();
    text.textProperty().bindBidirectional(component.textProperty());
    text.fontProperty().bindBidirectional(component.textFontProperty());
    text.fillProperty().bindBidirectional(component.textFillProperty());

    BufferedImage read = ImageIO.read(resourceLocationProvider.get("textures/gui/widgets.png").openInputStream());
    ImageView imageView = new ImageView(resample(SwingFXUtils.toFXImage(read.getSubimage(0, (int) (read.getHeight() * 66f / 256f), (int) (read.getWidth() * 200f / 256f), (int) (read.getHeight() * 20f / 256f)), null), (int) (256f / Math.min(read.getHeight(), read.getWidth()) * 4)));
    imageView.setSmooth(false);
    text.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("assets/minecraft/fonts/Minecraftia-Regular.ttf"), 32));
    text.setTranslateY(5);


    text.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, new Color(63f / 255f, 63f / 255f, 63f / 255f, 1), 1, 0, 4, 4));
    this.getChildren().addAll(imageView, text);
    this.getSkinnable().setStyle("-fx-text-fill: white");
  }

  @AssistedFactory(DefaultButtonSkin.class)
  public interface Factory {
    DefaultButtonSkin create(Button component);
  }

  private Image resample(Image input, int scaleFactor) {

    WritableImage output = new WritableImage(
        (int) input.getWidth() * scaleFactor,
        (int) input.getHeight() * scaleFactor
    );

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
