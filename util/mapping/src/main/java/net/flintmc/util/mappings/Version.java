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

package net.flintmc.util.mappings;

public final class Version {

  private Mapping modCoderPack;

  /**
   * Get the mod coder pack mapping.
   *
   * @return A mod coder pack mapping.
   */
  public Mapping getModCoderPack() {
    return modCoderPack;
  }

  public static class Mapping {

    private String configVersion;
    private String configDownload;
    private String mappingsVersion;
    private String mappingsDownload;

    /**
     * Get config version.
     *
     * @return A config version.
     */
    public String getConfigVersion() {
      return configVersion;
    }

    /**
     * Get config download URL.
     *
     * @return A config download URL.
     */
    public String getConfigDownload() {
      return configDownload;
    }

    /**
     * Get mapping version.
     *
     * @return A mapping version.
     */
    public String getMappingsVersion() {
      return mappingsVersion;
    }

    /**
     * Get mappings download URL.
     *
     * @return A mappings download URL.
     */
    public String getMappingsDownload() {
      return mappingsDownload;
    }
  }
}
