package assets.minecraft.labyfy.themes.component.button

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.SkinBase
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import net.labyfy.component.gui.component.GuiComponent
import net.labyfy.component.gui.mcjfxgl.McJfxGLApplication
import net.labyfy.component.gui.mcjfxgl.component.labeled.button.Button
import net.labyfy.component.inject.InjectionHolder
import net.labyfy.component.launcher.classloading.RootClassLoader
import net.labyfy.component.resources.ResourceLocationProvider

target = Button.class

class DefaultButtonSkin extends SkinBase<Button.Handle> implements GuiComponent {

    private final ResourceLocationProvider resourceLocationProvider;

    protected DefaultButtonSkin(Button.Handle control) {
        super(control)
        this.getChildren().clear()
        this.resourceLocationProvider = InjectionHolder.getInjectedInstance(ResourceLocationProvider.class);
        Button button = (Button) control.getComponent()

        this.getSkinnable().getStyleClass().setAll("button-primary");
        this.getSkinnable().getStylesheets().setAll(RootClassLoader.getSystemResource("assets/minecraft/labyfy/themes/component/button/button_default.css").toExternalForm())

        Text text = new Text();

        text.textProperty().bindBidirectional(button.textProperty());
        text.fontProperty().bindBidirectional(button.textFontProperty());
        text.fillProperty().bindBidirectional(button.textFillProperty());
        text.setFont(
                Font.loadFont(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream("assets/minecraft/fonts/Minecraftia-Regular.ttf"),
                        26));
        text.setTranslateY(5);

        text.setEffect(
                new DropShadow(
                        BlurType.ONE_PASS_BOX, new Color(63f / 255f, 63f / 255f, 63f / 255f, 1), 1, 0, 4, 4));

        this.getChildren().add(text)
    }


}

apply (Button.Handle control) -> new DefaultButtonSkin(control);