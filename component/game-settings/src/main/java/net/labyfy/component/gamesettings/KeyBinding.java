package net.labyfy.component.gamesettings;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface KeyBinding {

  String getKeyCategory();

  String getKeyDescription();

  int getKeyCode();

  boolean isPressed();

  void setPressed(boolean pressed);

  void bind(String keyName);

  boolean isInvalid();

  boolean matchesKey(int keyCode, int scanCode);

  boolean matchesMouseKey(int key);

  String getLocalizedName();

  boolean isDefault();

  String getTranslationKey();

  @AssistedFactory(KeyBinding.class)
  interface Factory {

    KeyBinding create(
            @Assisted("description") String description,
            @Assisted("keyCode") int keyCode,
            @Assisted("category") String category
    );
  }

}
