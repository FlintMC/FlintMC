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

package net.flintmc.render.gui.webgui.internal;

import net.flintmc.render.gui.webgui.WebResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class URLWebResource implements WebResource {

  private static final int BUFFER_SIZE = 512;

  private final String path;
  private final URL url;
  private final List<Byte> data;
  private long size = -1;
  private InputStream stream;

  public URLWebResource(String path, URL url) {
    this.path = path;
    this.url = url;
    this.data = new ArrayList<>();
  }

  public URLWebResource(String path) throws FileNotFoundException {
    this(path, URLWebResource.class.getResource("/" + path));
    if (this.url == null) {
      throw new FileNotFoundException("Couldn't find resource at " + path);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPath() {
    return this.path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMimeType() {
    return URLConnection.guessContentTypeFromName(this.url.toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void open() throws IOException {
    this.stream = this.url.openStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  private long readFile() throws IOException {
    if (!this.data.isEmpty()) {
      return this.data.size();
    }
    long total = 0;
    long read;
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
      this.size = this.readFile();
    }
    return this.size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long readFromFile(ByteBuffer data, long length) throws IOException {
    long read = this.readFile();
    for (byte b : this.data) {
      data.put(b);
    }

    return read;
  }
}
