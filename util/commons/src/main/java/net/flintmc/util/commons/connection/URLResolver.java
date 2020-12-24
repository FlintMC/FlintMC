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

package net.flintmc.util.commons.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLResolver {

  private URLResolver() {
  }

  /**
   * Opens a {@link HttpURLConnection} and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the resulting connection
   * @throws IOException if the connection could not be established
   */
  public static HttpURLConnection prepare(URL url) throws IOException {
    try {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("User-Agent", "Flint (+https://flintmc.net/)");
      return connection;
    } catch (IOException ex) {
      throw new IOException("Could not establish connection to url " + url, ex);
    }
  }

  /**
   * Opens and connects a {@link HttpURLConnection} and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the resulting connection
   * @throws IOException if the connection could not be established
   */
  public static HttpURLConnection connect(URL url) throws IOException {
    HttpURLConnection connection = prepare(url);
    try {
      connection.connect();
      return connection;
    } catch (IOException ex) {
      throw new IOException("Could not establish connection to url " + url, ex);
    }
  }

  /**
   * Opens an {@link InputStream} to a given url and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the payload
   * @throws IOException if the connection could not be established
   */
  public static InputStream resolve(URL url) throws IOException {
    return connect(url).getInputStream();
  }
}
