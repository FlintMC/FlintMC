package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.*;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.internal.chat.builder.*;
import net.flintmc.mcapi.internal.chat.component.DefaultKeybindComponent;
import net.minecraft.util.text.*;

import java.util.function.Supplier;

@Singleton
@Implement(value = MinecraftComponentMapper.class, version = "1.15.2")
public class VersionedMinecraftComponentMapper implements MinecraftComponentMapper {

  static {
    DefaultKeybindComponent.nameFunction =
        keybind -> {
          Supplier<String> supplier =
              KeybindTextComponent.displaySupplierFunction.apply(keybind.getKey());
          return supplier == null ? null : supplier.get();
        };
  }

  private final ComponentSerializer.Factory factory;

  @Inject
  public VersionedMinecraftComponentMapper(ComponentSerializer.Factory factory) {
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

    result.setStyle(this.createStyle(component));

    for (ChatComponent extra : component.extras()) {
      result.appendSibling((ITextComponent) this.toMinecraft(extra));
    }

    return result;
  }

  private ChatComponent createFlintComponent(ITextComponent component) {
    if (component instanceof KeybindTextComponent) {

      return new DefaultKeybindComponentBuilder()
              .keybind(Keybind.getByKey(((KeybindTextComponent) component).getKeybind()))
              .build();

    } else if (component instanceof ScoreTextComponent) {

      return new DefaultScoreComponentBuilder()
              .name(((ScoreTextComponent) component).getName())
          .objective(((ScoreTextComponent) component).getObjective())
          .build();

    } else if (component instanceof SelectorTextComponent) {

      try {
        return new DefaultSelectorComponentBuilder()
            .parse(((SelectorTextComponent) component).getSelector())
            .build();
      } catch (InvalidSelectorException ignored) {
      }

    } else if (component instanceof StringTextComponent) {

      return new DefaultTextComponentBuilder()
          .text(((StringTextComponent) component).getText())
          .build();

    } else if (component instanceof TranslationTextComponent) {

      Object[] arguments = ((TranslationTextComponent) component).getFormatArgs();
      ChatComponent[] mappedArguments = new ChatComponent[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
        Object argument = arguments[i];
        if (argument instanceof ITextComponent) {
          mappedArguments[i] = this.fromMinecraft(argument);
        }

        if (mappedArguments[i] == null) {
          mappedArguments[i] =
              new DefaultTextComponentBuilder().text(String.valueOf(argument)).build();
        }
      }

      return new DefaultTranslationComponentBuilder()
          .translationKey(((TranslationTextComponent) component).getKey())
          .arguments(mappedArguments)
          .build();
    }

    return null;
  }

  private void applyStyle(ChatComponent component, Style style) {
    if (style.getBold()) {
      component.toggleFormat(ChatFormat.BOLD, true);
    }
    if (style.getItalic()) {
      component.toggleFormat(ChatFormat.ITALIC, true);
    }
    if (style.getObfuscated()) {
      component.toggleFormat(ChatFormat.OBFUSCATED, true);
    }
    if (style.getStrikethrough()) {
      component.toggleFormat(ChatFormat.STRIKETHROUGH, true);
    }
    if (style.getUnderlined()) {
      component.toggleFormat(ChatFormat.UNDERLINED, true);
    }
    if (style.getColor() != null) {
      ChatColor color = ChatColor.getByName(style.getColor().name());
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
      HoverEvent.Action action =
          HoverEvent.Action.valueOf(style.getHoverEvent().getAction().name());
      ITextComponent value = style.getHoverEvent().getValue();

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

    } else if (component instanceof net.flintmc.mcapi.chat.component.TextComponent) {

      String text = ((net.minecraft.util.text.TextComponent) component).getFormattedText();
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
    Style style = new Style();

    style.setBold(component.hasFormat(ChatFormat.BOLD));
    style.setItalic(component.hasFormat(ChatFormat.ITALIC));
    style.setUnderlined(component.hasFormat(ChatFormat.UNDERLINED));
    style.setStrikethrough(component.hasFormat(ChatFormat.STRIKETHROUGH));
    style.setObfuscated(component.hasFormat(ChatFormat.OBFUSCATED));

    if (component.color().isDefaultColor()) {
      style.setColor(TextFormatting.valueOf(component.color().getName()));
    }

    if (component.clickEvent() != null) {
      try {
        style.setClickEvent(
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
            net.minecraft.util.text.event.HoverEvent.Action.valueOf(content.getAction().name());
      } catch (IllegalArgumentException ignored) {
      }

      if (action != null) {
        ChatComponent value = this.factory.gson().serializeHoverContent(content);

        style.setHoverEvent(
            new net.minecraft.util.text.event.HoverEvent(
                action, (ITextComponent) this.toMinecraft(value)));
      }
    }

    style.setInsertion(component.insertion());

    return style;
  }
}
