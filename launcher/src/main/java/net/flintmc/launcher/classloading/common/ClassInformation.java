package net.flintmc.launcher.classloading.common;

import java.net.URL;
import java.security.CodeSigner;

/** Utility class containing simple information about a class file. */
public class ClassInformation {
  private final URL resourceURL;
  private final byte[] classBytes;
  private final CodeSigner[] signers;

  /**
   * Constructs a new {@link ClassInformation} with a given url, content and signers
   *
   * @param resourceURL The {@link URL} the class content has been retrieved from
   * @param classBytes The byte content of the class
   * @param signers The signer information attached to the class
   */
  ClassInformation(URL resourceURL, byte[] classBytes, CodeSigner[] signers) {
    this.resourceURL = resourceURL;
    this.classBytes = classBytes;
    this.signers = signers;
  }

  /**
   * Retrieves the resource {@link URL} determining the origin of the class
   *
   * @return Where this class has been retrieved from
   */
  public URL getResourceURL() {
    return resourceURL;
  }

  /**
   * Retrieves the content of the class
   *
   * @return Raw content of the class file
   */
  public byte[] getClassBytes() {
    return classBytes;
  }

  /**
   * Retrieves the signer information attached to the class
   *
   * @return The attached signer information
   */
  public CodeSigner[] getSigners() {
    return signers;
  }
}
