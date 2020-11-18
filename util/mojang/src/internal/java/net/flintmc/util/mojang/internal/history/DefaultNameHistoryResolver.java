package net.flintmc.util.mojang.internal.history;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.mojang.history.NameHistory;
import net.flintmc.util.mojang.history.NameHistoryEntry;
import net.flintmc.util.mojang.history.NameHistoryResolver;
import net.flintmc.util.mojang.internal.cache.FileCache;
import org.apache.logging.log4j.Logger;

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
    return this.cache.get(
        uniqueId,
        NameHistory.class,
        () -> {
          Collection<NameHistoryEntry> entries = new HashSet<>();

          try {
            HttpURLConnection connection =
                (HttpURLConnection)
                    new URL(String.format(NAMES_URL, UUIDTypeAdapter.fromUUID(uniqueId)))
                        .openConnection();
            int code = connection.getResponseCode();
            if (code == 204) {
              return null;
            }
            if (code != 200) {
              throw new IllegalStateException(
                  String.format(
                      "An unknown error occurred while resolving profile with the UUID %s: %d",
                      uniqueId, code));
            }

            try (InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
              JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

              for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                String name = object.get("name").getAsString();

                entries.add(
                    object.has("changedToAt")
                        ? this.entryFactory.create(name)
                        : this.entryFactory.create(name, object.get("changedToAt").getAsLong()));
              }
            }
          } catch (IOException exception) {
            this.logger.trace("Failed to resolve profile properties");
          }

          return this.historyFactory.create(uniqueId, entries);
        },
        VALID_TIME);
  }
}
