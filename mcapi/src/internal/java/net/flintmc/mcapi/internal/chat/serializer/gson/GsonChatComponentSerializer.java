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

package net.flintmc.mcapi.internal.chat.serializer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.mcapi.chat.component.ScoreComponent;
import net.flintmc.mcapi.chat.component.SelectorComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.exception.InvalidChatColorException;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.internal.chat.builder.DefaultSelectorComponentBuilder;
import net.flintmc.mcapi.internal.chat.builder.DefaultTextComponentBuilder;
import net.flintmc.mcapi.internal.chat.component.DefaultKeybindComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultScoreComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultTextComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultTranslationComponent;
import org.apache.logging.log4j.Logger;

public class GsonChatComponentSerializer
    implements JsonSerializer<ChatComponent>, JsonDeserializer<ChatComponent> {

  private final Logger logger;

  public GsonChatComponentSerializer(Logger logger) {
    this.logger = logger;
  }

  // read everything that is the same in any type of component
  private void deserializeBasic(
      JsonObject object, ChatComponent component, JsonDeserializationContext context)
      throws JsonParseException {
    // the color of the component
    if (object.has("color")) {
      String color = object.get("color").getAsString();
      try {
        component.color(ChatColor.getByName(color.toUpperCase()));
      } catch (InvalidChatColorException exception) {
        this.logger.trace("Invalid color while deserializing a json component", exception);
      }
    }

    // the formats of the component
    for (ChatFormat format : ChatFormat.values()) {
      if (object.has(format.getLowerName())) {
        component.toggleFormat(format, object.get(format.getLowerName()).getAsBoolean());
      }
    }

    // the click event of the component
    if (object.has("clickEvent")) {
      JsonObject event = object.getAsJsonObject("clickEvent");
      String actionName = event.get("action").getAsString().toUpperCase();
      ClickEvent.Action action = null;

      try {
        action = ClickEvent.Action.valueOf(actionName);
      } catch (IllegalArgumentException exception) {
        this.logger.trace(
            "Invalid clickEvent action while deserializing a json component: " + actionName);
      }

      if (action != null) {
        component.clickEvent(ClickEvent.of(action, event.get("value").getAsString()));
      }
    }

    // the hover event of the component
    if (object.has("hoverEvent")) {
      component.hoverEvent(context.deserialize(object.get("hoverEvent"), HoverEvent.class));
    }

    // the additional components in the component
    if (object.has("extra")) {
      component.extras(context.deserialize(object.get("extra"), ChatComponent[].class));
    }
  }

  // write everything that is the same in any type of component
  public void serializeBasic(
      ChatComponent src, JsonObject object, JsonSerializationContext context) {
    // The default color is white
    if (src.color() != ChatColor.WHITE) {
      object.addProperty("color", src.color().getLowerName());
    }

    // All formats of the component
    for (ChatFormat format : ChatFormat.values()) {
      if (src.hasFormat(format)) {
        object.addProperty(format.getLowerName(), true);
      }
    }

    if (src.insertion() != null) {
      object.addProperty("insertion", src.insertion());
    }

    ChatComponent[] extras = src.extras();
    // If no extra is provided, no entry will be added to the json
    if (extras.length != 0) {
      object.add("extra", context.serialize(extras));
    }

    // The click event of the component
    if (src.clickEvent() != null) {
      JsonObject event = new JsonObject();
      event.addProperty("action", src.clickEvent().getAction().getLowerName());
      event.addProperty("value", src.clickEvent().getValue());
      object.add("clickEvent", event);
    }

    // The hover event of the component
    if (src.hoverEvent() != null) {
      object.add("hoverEvent", context.serialize(src.hoverEvent()));
    }
  }

  @Override
  public ChatComponent deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.isJsonPrimitive()) {
      return new DefaultTextComponentBuilder().text(json.getAsString()).build();
    }

    if (!json.isJsonObject()) {
      return null;
    }
    JsonObject object = json.getAsJsonObject();

    ChatComponent component = this.deserializeSpecific(object, context);
    if (component == null) {
      return null;
    }

    this.deserializeBasic(object, component, context);

    return component;
  }

  // define the type of the component depending on the content of the json
  private ChatComponent deserializeSpecific(JsonObject object, JsonDeserializationContext context) {
    if (object.has("text")) {
      // text components are identified by the "text" string
      TextComponent component = new DefaultTextComponent();
      component.text(object.get("text").getAsString());
      return component;
    } else if (object.has("translate")) {
      // translation components are identified by the "translate" string
      // and might contain "with" as an array of components for replacements in the translated text
      TranslationComponent component = new DefaultTranslationComponent();
      component.translationKey(object.get("translate").getAsString());
      if (object.has("with")) {
        component.arguments(context.deserialize(object.get("with"), ChatComponent[].class));
      }
      return component;
    } else if (object.has("keybind")) {
      // keybind components are identified by the "keybind" string
      Keybind keybind = Keybind.getByKey(object.get("keybind").getAsString());
      if (keybind == null) {
        return null;
      }

      KeybindComponent component = new DefaultKeybindComponent();
      component.keybind(keybind);
      return component;
    } else if (object.has("score")) {
      // score components are identified by a "score" object with a "name" and "objective
      JsonObject score = object.get("score").getAsJsonObject();
      if (!score.has("name") || !score.has("objective")) {
        return null;
      }

      ScoreComponent component = new DefaultScoreComponent();
      component.name(score.get("name").getAsString());
      component.objective(score.get("objective").getAsString());
      return component;
    } else if (object.has("selector")) {
      // selector components are identified by a "selector" string
      try {
        return new DefaultSelectorComponentBuilder()
            .parse(object.get("selector").getAsString())
            .build();
      } catch (InvalidSelectorException exception) {
        this.logger.trace(
            "Invalid selector received while parsing a selector component out of the json",
            exception);
        return null;
      }
    } else {
      // no component matching the given json found
      return null;
    }
  }

  @Override
  public JsonElement serialize(
      ChatComponent src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject object = new JsonObject();
    if (!this.serializeSpecific(object, src, context)) {
      return null;
    }

    this.serializeBasic(src, object, context);

    return object;
  }

  // define the type of the component in the json
  private boolean serializeSpecific(
      JsonObject object, ChatComponent component, JsonSerializationContext context) {
    if (component instanceof TextComponent) {
      String text = ((TextComponent) component).text();
      if (text == null) {
        // no text provided
        return false;
      }

      object.addProperty("text", text);
      return true;
    } else if (component instanceof TranslationComponent) {
      String translationKey = ((TranslationComponent) component).translationKey();
      ChatComponent[] arguments = ((TranslationComponent) component).arguments();
      if (translationKey == null) {
        // no translation key provided
        return false;
      }

      object.addProperty("translate", translationKey);
      if (arguments.length != 0) {
        object.add("with", context.serialize(arguments));
      }
      return true;
    } else if (component instanceof KeybindComponent) {
      Keybind keybind = ((KeybindComponent) component).keybind();
      if (keybind == null) {
        // no keybind provided
        return false;
      }

      object.addProperty("keybind", keybind.getKey());
      return true;
    } else if (component instanceof ScoreComponent) {
      String name = ((ScoreComponent) component).name();
      String objective = ((ScoreComponent) component).objective();
      if (name == null || objective == null) {
        // no name or objective provided
        return false;
      }

      JsonObject score = new JsonObject();
      score.addProperty("name", name);
      score.addProperty("objective", objective);
      object.add("score", score);

      return true;
    } else if (component instanceof SelectorComponent) {
      if (((SelectorComponent) component).selector() == null) {
        // no selector provided
        return false;
      }

      object.addProperty("selector", component.getUnformattedText());
      return true;
    }

    // custom component?
    return false;
  }
}
