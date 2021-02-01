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

package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.suggestion.Suggestion;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.suggestion.SuggestionList;
import net.flintmc.mcapi.chat.suggestion.SuggestionList.Factory;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.client.CTabCompletePacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@Singleton
@Implement(value = ChatController.class, version = "1.15.2")
public class VersionedChatController implements ChatController {

  private static final int MAX_MESSAGES = 100;
  private static final int MAX_INPUT_LENGTH = 256;

  private final MinecraftComponentMapper componentMapper;
  private final NetworkPlayerInfoRegistry playerInfoRegistry;

  private final SuggestionList.Factory suggestionListFactory;
  private final SuggestionList emptySuggestionList;
  private final Suggestion.Factory suggestionFactory;
  private final Map<Integer, CompletableFuture<SuggestionList>> pendingSuggestionRequests;
  private int currentSuggestionId = 1000;

  @Inject
  private VersionedChatController(
      MinecraftComponentMapper componentMapper,
      NetworkPlayerInfoRegistry playerInfoRegistry,
      Factory suggestionListFactory,
      Suggestion.Factory suggestionFactory) {
    this.componentMapper = componentMapper;
    this.playerInfoRegistry = playerInfoRegistry;
    this.suggestionListFactory = suggestionListFactory;
    this.emptySuggestionList = suggestionListFactory.create(0, 0, Collections.emptyList());
    this.suggestionFactory = suggestionFactory;
    this.pendingSuggestionRequests = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean dispatchChatInput(String message) {
    if (message.length() >= this.getChatInputLimit()) {
      // the message is longer than the maximum allowed, servers would kick the player when sending
      // this
      message = message.substring(0, this.getChatInputLimit());
    }

    Minecraft.getInstance().player.sendChatMessage(message);
    Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(message);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChatInputLimit() {
    return MAX_INPUT_LENGTH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayChatMessage(ChatLocation location, ChatComponent component) {
    if (Minecraft.getInstance().ingameGUI == null) {
      return;
    }

    ChatType type;

    switch (location) {
      case CHAT:
        type = ChatType.CHAT;
        break;
      case ACTION_BAR:
        type = ChatType.GAME_INFO;
        break;
      case SYSTEM:
        type = ChatType.SYSTEM;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + location);
    }

    ITextComponent mapped = (ITextComponent) this.componentMapper.toMinecraft(component);
    Minecraft.getInstance().ingameGUI.addChatMessage(type, mapped);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayChatMessage(
      ChatLocation location, ChatComponent component, UUID senderUniqueId) {
    this.displayChatMessage(location, component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getInputHistory() {
    if (Minecraft.getInstance().ingameGUI == null) {
      return new ArrayList<>();
    }
    return Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ChatComponent> getReceivedMessages() {
    ChatGuiShadow shadow = (ChatGuiShadow) Minecraft.getInstance().ingameGUI.getChatGUI();
    List<ChatComponent> components = new ArrayList<>(shadow.getLines().size());

    for (ChatLine line : shadow.getLines()) {
      components.add(this.componentMapper.fromMinecraft(line.getChatComponent()));
    }

    return components;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxMessages() {
    return MAX_MESSAGES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<SuggestionList> requestSuggestions(String input) {
    ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      throw new IllegalStateException("Cannot request suggestions if not connected to any server");
    }

    if (!input.startsWith("/")) {
      // only send commands to the server

      return CompletableFuture.completedFuture(this.createPlayerSuggestions(input));
    }

    if (input.length() > this.getChatInputLimit()) {
      input = input.substring(0, this.getChatInputLimit());
    }

    CompletableFuture<SuggestionList> future = new CompletableFuture<>();

    if (this.currentSuggestionId == Short.MAX_VALUE) {
      this.currentSuggestionId = 1000;
    }
    int suggestionId = ++this.currentSuggestionId;

    this.pendingSuggestionRequests.put(suggestionId, future);
    connection.sendPacket(new CTabCompletePacket(suggestionId, input));

    return future;
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.ClientSuggestionProvider",
      methodName = "handleResponse",
      parameters = {
          @Type(reference = int.class),
          @Type(typeName = "com.mojang.brigadier.suggestion.Suggestions")
      },
      version = "1.15.2",
      executionTime = ExecutionTime.BEFORE)
  public HookResult handleCompletionResponse(@Named("args") Object[] args) {
    int transactionId = (int) args[0];

    if (!this.pendingSuggestionRequests.containsKey(transactionId)) {
      return HookResult.CONTINUE; // request sent by Minecraft, continue their handling
    }

    CompletableFuture<SuggestionList> future = this.pendingSuggestionRequests.remove(transactionId);

    SuggestionList list = this.mapSuggestions((Suggestions) args[1]);
    future.complete(list);

    return HookResult.BREAK; // request not sent by Minecraft
  }

  private SuggestionList mapSuggestions(Suggestions handle) {
    List<Suggestion> suggestions = new ArrayList<>();
    for (com.mojang.brigadier.suggestion.Suggestion suggestion : handle.getList()) {
      ChatComponent tooltip = suggestion.getTooltip() == null ? null
          : this.componentMapper.fromMinecraft(suggestion.getTooltip());
      suggestions.add(this.suggestionFactory.create(suggestion.getText(), tooltip));
    }

    StringRange range = handle.getRange();
    return this.suggestionListFactory.create(range.getStart(), range.getEnd(), suggestions);
  }

  private SuggestionList createPlayerSuggestions(String input) {
    int lastSpace = input.lastIndexOf(' ');
    if (lastSpace != -1) {
      ++lastSpace;
    }
    if (lastSpace >= input.length()) {
      return this.emptySuggestionList;
    }

    String prefix =
        lastSpace == -1 ? input : input.substring(lastSpace);
    if (prefix.isEmpty()) {
      return this.emptySuggestionList;
    }

    int startIndex = lastSpace == -1 ? 0 : lastSpace;
    int endIndex = startIndex + prefix.length();

    return this.createPlayerSuggestions(prefix, startIndex, endIndex);
  }

  private SuggestionList createPlayerSuggestions(String prefix, int startIndex, int endIndex) {
    List<Suggestion> suggestions = new ArrayList<>();

    String lowerPrefix = prefix != null ? prefix.toLowerCase() : null;

    for (NetworkPlayerInfo info : this.playerInfoRegistry.getPlayerInfoMap().values()) {
      String name = info.getGameProfile().getName();
      if (this.containsText(suggestions, name)) {
        continue;
      }

      if (lowerPrefix == null || name.toLowerCase().startsWith(lowerPrefix)) {
        suggestions.add(this.suggestionFactory.create(name, null));
      }
    }

    return this.suggestionListFactory.create(startIndex, endIndex, suggestions);
  }

  private boolean containsText(List<Suggestion> suggestions, String text) {
    for (Suggestion suggestion : suggestions) {
      if (suggestion.getText().equals(text)) {
        return true;
      }
    }

    return false;
  }

}
