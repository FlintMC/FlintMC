package net.labyfy.component.launcher.classloading.common;

import java.net.URL;
import java.security.CodeSigner;

public class ClassInformation {
  private final URL resourceURL;
  private final byte[] classBytes;
  private final CodeSigner[] signers;

  ClassInformation(URL resourceURL, byte[] classBytes, CodeSigner[] signers) {
    this.resourceURL = resourceURL;
    this.classBytes = classBytes;
    this.signers = signers;
  }

  public URL getResourceURL() {
    return resourceURL;
  }

  public byte[] getClassBytes() {
    return classBytes;
  }

  public CodeSigner[] getSigners() {
    return signers;
  }
}
