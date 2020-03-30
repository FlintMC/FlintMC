package net.labyfy.component.gui.mcjfxgl.component.style.css;

public class ResourceProvider {

  private static final String CSS_LOCATION = "assets/css/";

  private ResourceProvider() {}

  public static String getCSSResource(String css) {
    return ResourceProvider.class.getClassLoader().getResource(CSS_LOCATION + css).toExternalForm();
  }
}
