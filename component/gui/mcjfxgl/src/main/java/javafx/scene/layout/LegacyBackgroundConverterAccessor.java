package javafx.scene.layout;

public class LegacyBackgroundConverterAccessor {
  private LegacyBackgroundConverterAccessor() {
  }

  public static BackgroundConverter get() {
    return (BackgroundConverter) BackgroundConverter.INSTANCE;
  }
}
