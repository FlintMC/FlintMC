package net.labyfy.component.gui.mcjfxgl.component.theme.source;

import java.net.URL;
import java.security.CodeSource;
import java.security.cert.Certificate;

public class ThemeCodeSource extends CodeSource {
  public ThemeCodeSource(URL url, Certificate[] certs) {
    super(url, certs);
  }
}
