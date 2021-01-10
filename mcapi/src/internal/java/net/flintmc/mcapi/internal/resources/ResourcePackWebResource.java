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

package net.flintmc.mcapi.internal.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.render.gui.webgui.WebResource;

public class ResourcePackWebResource implements WebResource {

  private static final int BUFFER_SIZE = 512;

  private final ResourceLocation location;
  private final List<Byte> data;
  private InputStream stream;
  private long size;

  protected ResourcePackWebResource(ResourceLocation location) {
    this.location = location;
    this.data = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPath() {
    return this.location.getNamespace() + ':' + this.location.getPath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMimeType() {
    return URLConnection.guessContentTypeFromName(this.location.getPath());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void open() throws IOException {
    this.stream = this.location.openInputStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  private int readFile() throws IOException {
    if (!this.data.isEmpty()) {
      return this.data.size();
    }
    int total = 0;
    int read;
    byte[] buffer = new byte[BUFFER_SIZE];
    while ((read = this.stream.read(buffer)) != -1) {
      total += read;
      for (int i = 0; i < read; i++) {
        this.data.add(buffer[i]);
      }
    }

    return total;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getSize() throws IOException {
    if (this.size < 0) {
      this.size = readFile();
    }
    return this.size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long readFromFile(ByteBuffer data, long length) throws IOException {
    int read = readFile();
    for (byte b : this.data) {
      data.put(b);
    }

    return read;
  }
}
