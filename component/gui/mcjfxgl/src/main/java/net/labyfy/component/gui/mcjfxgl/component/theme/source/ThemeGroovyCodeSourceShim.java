package net.labyfy.component.gui.mcjfxgl.component.theme.source;

import groovy.lang.GroovyCodeSource;

import java.security.CodeSource;

public class ThemeGroovyCodeSourceShim extends GroovyCodeSource {
  private final ThemeCodeSource codeSource;

  public ThemeGroovyCodeSourceShim(String script, String name) {
    super(script, name, "/groovy/theme-shell");
    this.codeSource = new ThemeCodeSource(getCodeSource().getLocation(), getCodeSource().getCertificates());
  }

  @Override
  public CodeSource getCodeSource() {
    return codeSource;
  }
}
