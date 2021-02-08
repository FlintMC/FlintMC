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

package net.flintmc.util.mojang.internal.history;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.mojang.MojangRateLimitException;
import net.flintmc.util.mojang.UUIDParser;
import net.flintmc.util.mojang.history.NameHistory;
import net.flintmc.util.mojang.history.NameHistoryEntry;
import net.flintmc.util.mojang.history.NameHistoryResolver;
import net.flintmc.util.mojang.internal.cache.FileCache;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(NameHistoryResolver.class)
public class DefaultNameHistoryResolver implements NameHistoryResolver {

  private static final long VALID_TIME = TimeUnit.MINUTES.toMillis(10);
  private static final String NAMES_URL = "https://api.mojang.com/user/profiles/%s/names";

  private final Logger logger;
  private final FileCache cache;
  private final NameHistory.Factory historyFactory;
  private final NameHistoryEntry.Factory entryFactory;

  @Inject
  private DefaultNameHistoryResolver(
      @InjectLogger Logger logger,
      FileCache cache,
      NameHistory.Factory historyFactory,
      NameHistoryEntry.Factory entryFactory) {
    this.logger = logger;
    this.cache = cache;
    this.historyFactory = historyFactory;
    this.entryFactory = entryFactory;
  }

  @Override
  public CompletableFuture<NameHistory> resolveHistory(UUID uniqueId) {
    NameHistory cached = this.cache.getCached(NameHistory.class, uniqueId);
    if (cached != null) {
      return CompletableFuture.completedFuture(cached);
    }

    return CompletableFuture.supplyAsync(
        () -> {
          Collection<NameHistoryEntry> entries = null;

          try {
            entries = this.fillEntries(uniqueId);
          } catch (IOException exception) {
            this.logger.trace("Failed to resolve name history for UUID " + uniqueId, exception);
          }

          NameHistory history =
              entries != null ? this.historyFactory.create(uniqueId, entries) : null;

          this.cache.cache(uniqueId, NameHistory.class, history, VALID_TIME);

          return history;
        });
  }

  private Collection<NameHistoryEntry> fillEntries(UUID uniqueId) throws IOException {
    HttpURLConnection connection =
        (HttpURLConnection)
            new URL(String.format(NAMES_URL, UUIDParser.toUndashedString(uniqueId))).openConnection();
    int code = connection.getResponseCode();
    if (code == 204) {
      return null;
    }
    if (code == 429) {
      throw MojangRateLimitException.INSTANCE;
    }
    if (code != 200) {
      throw new IllegalStateException(
          String.format(
              "An unknown error occurred while resolving profile with the UUID %s: %d",
              uniqueId, code));
    }

    Collection<NameHistoryEntry> entries = new HashSet<>();

    try (InputStream inputStream = connection.getInputStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

      for (JsonElement element : array) {
        JsonObject object = element.getAsJsonObject();
        String name = object.get("name").getAsString();

        entries.add(
            object.has("changedToAt")
                ? this.entryFactory.create(name, object.get("changedToAt").getAsLong())
                : this.entryFactory.create(name));
      }
    }

    return entries;
  }
}
