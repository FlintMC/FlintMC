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
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
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
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverEntity;
import net.flintmc.mcapi.chat.component.event.content.HoverItem;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.text.event.HoverEvent.EntityHover;
import net.minecraft.util.text.event.HoverEvent.ItemHover;

@Singleton
@Implement(MinecraftComponentMapper.class)
public class VersionedMinecraftComponentMapper implements MinecraftComponentMapper {

  private final ComponentBuilder.Factory builderFactory;
  private final ComponentSerializer.Factory factory;
  private final ClickEvent.Factory clickEventFactory;
  private final HoverEvent.Factory hoverEventFactory;

  private final MinecraftItemMapper itemMapper;

  @Inject
  public VersionedMinecraftComponentMapper(
      Factory builderFactory,
      ComponentSerializer.Factory factory,
      ClickEvent.Factory clickEventFactory,
      HoverEvent.Factory hoverEventFactory,
      final MinecraftItemMapper itemMapper) {
    this.builderFactory = builderFactory;
    this.factory = factory;
    this.clickEventFactory = clickEventFactory;
    this.hoverEventFactory = hoverEventFactory;
    this.itemMapper = itemMapper;
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
      Object textComponent = this.toMinecraft(extra);

      if (textComponent == null) {
        textComponent = ITextComponent.getTextComponentOrEmpty("");
      }

      ((IFormattableTextComponent) result).appendSibling((ITextComponent) textComponent);
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
      component.clickEvent(this.clickEventFactory.create(
          this.mapClickEventAction(style.getClickEvent().getAction()),
          style.getClickEvent().getValue()));
    }

    if (style.getHoverEvent() != null) {
      net.minecraft.util.text.event.HoverEvent hoverEvent = style.getHoverEvent();
      AccessibleHoverEvent accessibleHoverEvent = (AccessibleHoverEvent) hoverEvent;

      HoverEvent.Action action = this.mapHoverEventAction(hoverEvent.getAction());

      Object hoverEventValue = accessibleHoverEvent.getValue();
      ITextComponent value;

      if (hoverEventValue instanceof EntityHover) {
        value = ((EntityHover) hoverEventValue).name;
      } else if (hoverEventValue instanceof ItemHover) {
        AccessibleItemHover itemHover = (AccessibleItemHover) hoverEventValue;
        value = new StringTextComponent(itemHover.getSerializeElement().toString());
      } else {
        value = (ITextComponent) hoverEventValue;
      }

      HoverContent content =
          this.factory.gson().deserializeHoverContent(this.fromMinecraft(value), action);

      if (content != null) {
        component.hoverEvent(this.hoverEventFactory.create(content));
      }
    }

    component.insertion(style.getInsertion());
  }

  private ClickEvent.Action mapClickEventAction(
      net.minecraft.util.text.event.ClickEvent.Action action) {
    switch (action) {
      case OPEN_URL:
        return Action.OPEN_URL;
      case OPEN_FILE:
        return Action.OPEN_FILE;
      case RUN_COMMAND:
        return Action.RUN_COMMAND;
      case CHANGE_PAGE:
        return Action.CHANGE_PAGE;
      case COPY_TO_CLIPBOARD:
        return Action.COPY_TO_CLIPBOARD;
      default:
      case SUGGEST_COMMAND:
        return Action.SUGGEST_COMMAND;
    }
  }

  private net.minecraft.util.text.event.ClickEvent.Action mapClickEventAction(
      ClickEvent.Action action) {
    switch (action) {
      case OPEN_URL:
        return net.minecraft.util.text.event.ClickEvent.Action.OPEN_URL;
      case OPEN_FILE:
        return net.minecraft.util.text.event.ClickEvent.Action.OPEN_FILE;
      case RUN_COMMAND:
        return net.minecraft.util.text.event.ClickEvent.Action.RUN_COMMAND;
      case CHANGE_PAGE:
        return net.minecraft.util.text.event.ClickEvent.Action.CHANGE_PAGE;
      case COPY_TO_CLIPBOARD:
        return net.minecraft.util.text.event.ClickEvent.Action.COPY_TO_CLIPBOARD;
      default:
      case SUGGEST_COMMAND:
        return net.minecraft.util.text.event.ClickEvent.Action.SUGGEST_COMMAND;
    }
  }

  private HoverEvent.Action mapHoverEventAction(
      net.minecraft.util.text.event.HoverEvent.Action<?> action) {
    if (action == net.minecraft.util.text.event.HoverEvent.Action.SHOW_ENTITY) {
      return HoverEvent.Action.SHOW_ENTITY;
    } else if (action == net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT) {
      return HoverEvent.Action.SHOW_TEXT;
    } else if (action == net.minecraft.util.text.event.HoverEvent.Action.SHOW_ITEM) {
      return HoverEvent.Action.SHOW_ITEM;
    }

    return HoverEvent.Action.SHOW_TEXT;
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
      style = style.setColor(
          Color.fromTextFormatting(TextFormatting.getValueByName(component.color().getName())));
    } else {
      style = style.setColor(Color.fromInt(component.color().getRgb()));
    }

    if (component.clickEvent() != null) {
      try {
        style = style.setClickEvent(
            new net.minecraft.util.text.event.ClickEvent(
                this.mapClickEventAction(component.clickEvent().getAction()),
                component.clickEvent().getValue()));
      } catch (IllegalArgumentException ignored) {
      }
    }

    if (component.hoverEvent() != null && component.hoverEvent().getContents().length > 0) {
      net.minecraft.util.text.event.HoverEvent.Action action = null;
      HoverContent content = component.hoverEvent().getContents()[0];

      try {
        action = net.minecraft.util.text.event.HoverEvent.Action.getValueByCanonicalName(
            content.getAction().name().toLowerCase(Locale.ROOT));
      } catch (IllegalArgumentException ignored) {
      }

      if (action != null) {
        if (content.getAction() == HoverEvent.Action.SHOW_ENTITY) {
          HoverEntity hoverEntity = (HoverEntity) content;
          style = style.setHoverEvent(
              this.createEntityHover(hoverEntity.getType(), hoverEntity.getUniqueId(),
                  hoverEntity.getDisplayName()));
        } else if (content.getAction() == HoverEvent.Action.SHOW_ITEM) {
          HoverItem hoverItem = (HoverItem) content;
          style = style.setHoverEvent(this.createItemHover(hoverItem.getItemStack()));
        } else {
          ChatComponent value = this.factory.gson().serializeHoverContent(content);

          style = style.setHoverEvent(
              new net.minecraft.util.text.event.HoverEvent(action, this.toMinecraft(value)));
        }
      }
    }

    style = style.setInsertion(component.insertion());

    return style;
  }

  private net.minecraft.util.text.event.HoverEvent createItemHover(final
  net.flintmc.mcapi.items.ItemStack itemStack) {

    return new net.minecraft.util.text.event.HoverEvent(
        net.minecraft.util.text.event.HoverEvent.Action.SHOW_ITEM,
        new ItemHover((ItemStack) this.itemMapper.toMinecraft(itemStack))
    );
  }

  private net.minecraft.util.text.event.HoverEvent createEntityHover(final String name,
      final UUID uniqueId, final ChatComponent component) {
    Optional<EntityType<?>> entityType = EntityType.byKey(name);

    if (!entityType.isPresent()) {
      return null;
    }

    return new net.minecraft.util.text.event.HoverEvent(
        net.minecraft.util.text.event.HoverEvent.Action.SHOW_ENTITY,
        new EntityHover(
            entityType.get(),
            uniqueId,
            (ITextComponent) this.toMinecraft(component)
        )
    );
  }
}
