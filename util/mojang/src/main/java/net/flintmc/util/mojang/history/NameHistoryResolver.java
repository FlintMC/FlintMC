package net.flintmc.util.mojang.history;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NameHistoryResolver {

  CompletableFuture<NameHistory> resolveHistory(UUID uniqueId);
}
