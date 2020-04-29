package assets.minecraft.labyfy.themes.menu

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import net.labyfy.component.gui.adapter.GuiAdapter
import net.labyfy.component.gui.mcjfxgl.McJfxGLApplication
import net.labyfy.component.gui.mcjfxgl.labymod.MainMenu
import net.labyfy.component.gui.mcjfxgl.labymod.MinecraftMainMenuSkin
import net.labyfy.component.inject.InjectionHolder
import net.labyfy.component.resources.ResourceLocationProvider
import net.minecraft.launchwrapper.Launch

target = MainMenu.class

class DefaultMainMenuSkin extends MinecraftMainMenuSkin {

    private final ResourceLocationProvider resourceLocationProvider;
    private final McJfxGLApplication application;
    private final ImageView logo;
    private final ImageView backgroundImage;

    protected DefaultMainMenuSkin(MainMenu.Handle control) {
        super(control)

        this.application = InjectionHolder.getInjectedInstance(McJfxGLApplication.class)
        this.resourceLocationProvider = InjectionHolder.getInjectedInstance(ResourceLocationProvider.class)

        this.logo = new ImageView(new Image(this.resourceLocationProvider.get("labymod/textures/gui/logo.png").openInputStream()))

        this.logo.setPreserveRatio(true);
        this.logo.setFitWidth(250);

        this.backgroundImage = new ImageView(new Image(this.resourceLocationProvider.get("labymod/textures/gui/background.png").openInputStream()));
        this.stackPane.getStyleClass().setAll("main-menu")
        this.stackPane.getStylesheets().setAll(Launch.classLoader.getResource("assets/minecraft/labyfy/themes/menu/main_menu.css").toExternalForm())
        this.stackPane.getChildren().add(0, this.backgroundImage);
        this.stackPane.getChildren().add(this.logo);
    }

    void render(GuiAdapter adapter) {
        super.render(adapter)

        this.backgroundImage.setPreserveRatio(true);

        this.logo.setTranslateX(this.minecraftWindow.getScaledWidth() * 2 - 125)
        this.logo.setTranslateY((this.minecraftWindow.getScaledHeight() / 4 - 33) * 4)

        if (this.backgroundImage.getImage().getHeight() / minecraftWindow.getScaledHeight() < this.backgroundImage.getImage().getWidth() / minecraftWindow.getScaledWidth()) {
            this.backgroundImage.setFitHeight(minecraftWindow.getScaledHeight() * 4);
            this.backgroundImage.setFitWidth(0);
        } else {
            this.backgroundImage.setFitHeight(0);
            this.backgroundImage.setFitWidth(minecraftWindow.getScaledWidth() * 4);
        }
    }
}

apply (MainMenu.Handle control) -> new DefaultMainMenuSkin(control);