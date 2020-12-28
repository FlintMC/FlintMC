/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
