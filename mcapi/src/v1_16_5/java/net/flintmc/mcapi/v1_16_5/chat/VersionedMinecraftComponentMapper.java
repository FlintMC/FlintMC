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

package net.flintmc.mcapi.v1_16_5.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.builder.ComponentBuilder.Factory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.mcapi.chat.component.ScoreComponent;
import net.flintmc.mcapi.chat.component.SelectorComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.ScoreTextComponent;
import net.minecraft.util.text.SelectorTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@Implement(MinecraftComponentMapper.class)
public class VersionedMinecraftComponentMapper implements MinecraftComponentMapper {

  private final ComponentBuilder.Factory builderFactory;
  private final ComponentSerializer.Factory factory;

  @Inject
  public VersionedMinecraftComponentMapper(
      Factory builderFactory, ComponentSerializer.Factory factory) {
    this.builderFactory = builderFactory;
    this.factory = factory;
  }

  @Override
  public ChatComponent fromMinecraft(Object handle) {
    // only the ITextComponents by Minecraft are allowed
    if (!(handle instanceof ITextComponent)) {
      throw new ComponentDeserializationException(
          handle.getClass().getName() + " is not an instance of " + ITextComponent.class.getName());
    }

    ITextComponent component = (ITextComponent) handle;
    ChatComponent result = this.createFlintComponent(component);

    if (result == null) {
      return null;
    }

    this.applyStyle(result, component.getStyle());

    for (ITextComponent sibling : component.getSiblings()) {
      result.append(this.fromMinecraft(sibling));
    }

    return result;
  }

  @Override
  public Object toMinecraft(ChatComponent component) {
    ITextComponent result = this.createMinecraftComponent(component);

    if (result == null) {
      return null;
    }

    TextComponentUtils.func_240648_a_(
        (IFormattableTextComponent) result, this.createStyle(component));

    for (ChatComponent extra : component.extras()) {
      ((IFormattableTextComponent) result).append((ITextComponent) this.toMinecraft(extra));
    }

    return result;
  }

  private ChatComponent createFlintComponent(ITextComponent component) {
    if (component instanceof KeybindTextComponent) {

      return this.builderFactory.keybind()
          .keybind(Keybind.getByKey(((KeybindTextComponent) component).getKeybind()))
          .build();

    } else if (component instanceof ScoreTextComponent) {

      return this.builderFactory.score()
          .name(((ScoreTextComponent) component).getName())
          .objective(((ScoreTextComponent) component).getObjective())
          .build();

    } else if (component instanceof SelectorTextComponent) {

      try {
        return this.builderFactory.selector()
            .parse(((SelectorTextComponent) component).getSelector())
            .build();
      } catch (InvalidSelectorException ignored) {
      }

    } else if (component instanceof StringTextComponent) {

      return this.parseText(((StringTextComponent) component).getText());

    } else if (component instanceof TranslationTextComponent) {

      Object[] arguments = ((TranslationTextComponent) component).getFormatArgs();
      ChatComponent[] mappedArguments = new ChatComponent[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
        Object argument = arguments[i];
        if (argument instanceof ITextComponent) {
          mappedArguments[i] = this.fromMinecraft(argument);
        }

        if (mappedArguments[i] == null) {
          mappedArguments[i] = this.parseText(String.valueOf(argument));
        }
      }

      return this.builderFactory.translation()
          .translationKey(((TranslationTextComponent) component).getKey())
          .arguments(mappedArguments)
          .build();
    }

    return null;
  }

  private ChatComponent parseText(String text) {
    // we need to deserialize this as some servers still use the old format
    // and send something like '§atest' in the text
    return this.factory.legacy().deserialize(text);
  }

  private void applyStyle(ChatComponent component, Style style) {
    if (style.getBold()) {
      component.toggleChatFormat(ChatColor.BOLD, true);
    }
    if (style.getItalic()) {
      component.toggleChatFormat(ChatColor.ITALIC, true);
    }
    if (style.getObfuscated()) {
      component.toggleChatFormat(ChatColor.OBFUSCATED, true);
    }
    if (style.getStrikethrough()) {
      component.toggleChatFormat(ChatColor.STRIKETHROUGH, true);
    }
    if (style.getUnderlined()) {
      component.toggleChatFormat(ChatColor.UNDERLINED, true);
    }
    if (style.getColor() != null) {
      String coloredString = style.getColor().getName();

      ChatColor color =
          coloredString.startsWith("#")
              ? ChatColor.parse(coloredString)
              : ChatColor.getByName(coloredString.toUpperCase());
      if (color != null) {
        component.color(color);
      }
    }

    if (style.getClickEvent() != null) {
      component.clickEvent(
          ClickEvent.of(
              ClickEvent.Action.valueOf(style.getClickEvent().getAction().name()),
              style.getClickEvent().getValue()));
    }

    if (style.getHoverEvent() != null) {
      net.minecraft.util.text.event.HoverEvent hoverEvent = style.getHoverEvent();
      net.minecraft.util.text.event.HoverEvent.Action<?> hoverAction = hoverEvent.getAction();

      HoverEvent.Action action = HoverEvent.Action
          .valueOf(hoverAction.getCanonicalName().toUpperCase());

      AccessibleHoverEvent accessibleHoverEvent = (AccessibleHoverEvent) hoverEvent;

      ITextComponent value = (ITextComponent) accessibleHoverEvent.getValue();

      HoverContent content =
          this.factory.gson().deserializeHoverContent(this.fromMinecraft(value), action);

      if (content != null) {
        component.hoverEvent(HoverEvent.of(content));
      }
    }

    component.insertion(style.getInsertion());
  }

  private ITextComponent createMinecraftComponent(ChatComponent component) {
    if (component instanceof KeybindComponent) {

      Keybind keybind = ((KeybindComponent) component).keybind();
      if (keybind != null) {
        return new KeybindTextComponent(keybind.getKey());
      }

    } else if (component instanceof ScoreComponent) {

      String name = ((ScoreComponent) component).name();
      String objective = ((ScoreComponent) component).objective();

      if (name != null && objective != null) {
        return new ScoreTextComponent(name, objective);
      }

    } else if (component instanceof SelectorComponent) {

      EntitySelector selector = ((SelectorComponent) component).selector();

      if (selector != null) {
        return new SelectorTextComponent(component.getUnformattedText());
      }

    } else if (component instanceof TextComponent) {

      String text = ((TextComponent) component).text();
      if (text != null) {
        return new StringTextComponent(text);
      }

    } else if (component instanceof TranslationComponent) {

      String key = ((TranslationComponent) component).translationKey();
      ChatComponent[] arguments = ((TranslationComponent) component).arguments();

      if (key != null) {
        Object[] handleArguments = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
          ChatComponent argument = arguments[i];
          handleArguments[i] = argument != null ? this.toMinecraft(argument) : "null";
        }

        return new TranslationTextComponent(key, handleArguments);
      }
    }

    return null;
  }

  private Style createStyle(ChatComponent component) {
    Style style = Style.EMPTY;
    style = style.setBold(component.hasChatFormat(ChatColor.BOLD));
    style = style.setItalic(component.hasChatFormat(ChatColor.ITALIC));
    style = style.func_244282_c(component.hasChatFormat(ChatColor.UNDERLINED));
    if (component.hasChatFormat(ChatColor.STRIKETHROUGH)) {
      style = style.applyFormatting(TextFormatting.STRIKETHROUGH);
    }
    if (component.hasChatFormat(ChatColor.OBFUSCATED)) {
      style = style.applyFormatting(TextFormatting.OBFUSCATED);
    }

    if (component.color().isDefaultColor()) {
      style = style.setColor(Color.fromTextFormatting(TextFormatting.getValueByName(component.color().getName())));
    } else {
      style = style.setColor(Color.fromInt(component.color().getRgb()));
    }

    if (component.clickEvent() != null) {
      try {
        style = style.setClickEvent(
            new net.minecraft.util.text.event.ClickEvent(
                net.minecraft.util.text.event.ClickEvent.Action.valueOf(
                    component.clickEvent().getAction().name()),
                component.clickEvent().getValue()));
      } catch (IllegalArgumentException ignored) {
      }
    }

    if (component.hoverEvent() != null && component.hoverEvent().getContents().length > 0) {
      net.minecraft.util.text.event.HoverEvent.Action action = null;
      HoverContent content = component.hoverEvent().getContents()[0];

      try {
        action =
            net.minecraft.util.text.event.HoverEvent.Action.getValueByCanonicalName(
                content.getAction().name());
      } catch (IllegalArgumentException ignored) {
      }

      if (action != null) {
        ChatComponent value = this.factory.gson().serializeHoverContent(content);

        style = style.setHoverEvent(
            new net.minecraft.util.text.event.HoverEvent(action, this.toMinecraft(value)));
      }
    }

    style = style.setInsertion(component.insertion());

    return style;
  }
}
