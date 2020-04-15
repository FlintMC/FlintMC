package net.labyfy.component.gui.mcjfxgl.component.labeled.button;

import com.google.common.collect.Sets;
import com.google.inject.assistedinject.AssistedInject;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.Skin;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.labeled.Labeled;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collection;

@IgnoreInitialization
public final class Button extends Labeled<Button> {

  private final MinecraftButtonSkin.Factory defaultSkinFactory;

  @AssistedInject
  private Button(MinecraftButtonSkin.Factory defaultSkinFactory) {
    this.defaultSkinFactory = defaultSkinFactory;
  }

  public McJfxGLControl createControl() {
    return new Handle(this);
  }

  @AssistedFactory(Button.class)
  public interface Factory {
    Button create();
  }

  @IgnoreInitialization
  public static class Handle extends Labeled.Handle {
    private final Button component;

    protected Handle(Button component) {
      super(component);
      this.component = component;
    }

    protected Skin<?> createDefaultSkin() {
      return this.component.defaultSkinFactory.create(this.component);
    }

    protected Class<? extends Skin<? extends McJfxGLControl>> getDefaultSkinClass() {
      return MinecraftButtonSkin.class;
    }

    public void render(GuiAdapter adapter) {
      super.render(adapter);
    }

    public Collection<CssMetaData<? extends Styleable, ?>> getControlClassMetaData() {
      Collection<CssMetaData<? extends Styleable, ?>> cssMetaData =
          Sets.newHashSet(super.getControlClassMetaData());
      cssMetaData.add(component.textFontProperty().getCssMetaData());
      cssMetaData.add(component.textFillProperty().getCssMetaData());
      return cssMetaData;
    }
  }
}
