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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Singleton
public class McpMappingIndexProvider {

  private static final String INDEX_URL = "https://dl.labymod.net/mappings/index_new.json";
  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  /**
   * Fetch information about Minecraft mappings.
   *
   * @return A version &lt;-&gt; mapping information map.
   * @throws IOException If the mapping information could not be retrieved.
   */
  public Map<String, Version> fetch() throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(INDEX_URL).openConnection();
    connection.setRequestProperty("User-Agent", "Flint (+https://flintmc.net/)");
    connection.connect();

    try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
      return gson.fromJson(reader, new TypeToken<Map<String, Version>>() {
      }.getType());
    }
  }
}
