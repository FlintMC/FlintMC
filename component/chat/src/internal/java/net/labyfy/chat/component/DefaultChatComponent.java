package net.labyfy.chat.component;

import net.labyfy.chat.component.event.ClickEvent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.chat.format.ChatFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class DefaultChatComponent implements ChatComponent {

  private final boolean[] formats = new boolean[ChatFormat.values().length]; // all formats sorted by the order in the enum

  private final List<ChatComponent> extras = new ArrayList<>();

  private ChatColor color;

  private String insertion;

  private ClickEvent clickEvent;
  private HoverEvent hoverEvent;

  @Override
  public ChatFormat[] formats() {
    Collection<ChatFormat> formats = new ArrayList<>();
    for (int i = 0; i < this.formats.length; i++) {
      if (this.formats[i]) {
        formats.add(ChatFormat.values()[i]);
      }
    }

    return formats.toArray(new ChatFormat[0]);
  }

  @Override
  public boolean hasFormat(ChatFormat format) {
    return this.formats[format.ordinal()];
  }

  @Override
  public void toggleFormat(ChatFormat format, boolean enabled) {
    this.formats[format.ordinal()] = enabled;
  }

  @Override
  public ChatColor color() {
    return this.color == null ? ChatColor.WHITE : this.color;
  }

  @Override
  public void color(ChatColor color) {
    this.color = color;
  }

  @Override
  public String insertion() {
    return this.insertion;
  }

  @Override
  public void insertion(String insertion) {
    this.insertion = insertion;
  }

  @Override
  public ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Override
  public void clickEvent(ClickEvent event) {
    this.clickEvent = event;
  }

  @Override
  public HoverEvent hoverEvent() {
    return this.hoverEvent;
  }

  @Override
  public void hoverEvent(HoverEvent event) {
    this.hoverEvent = event;
  }

  @Override
  public ChatComponent[] extras() {
    return this.extras.toArray(new ChatComponent[0]);
  }

  @Override
  public void extras(ChatComponent[] extras) {
    this.extras.clear();
    this.extras.addAll(Arrays.asList(extras));
  }

  @Override
  public ChatComponent copy() {
    DefaultChatComponent copy = this.createCopy();

    System.arraycopy(this.formats, 0, copy.formats, 0, this.formats.length);

    for (ChatComponent extra : this.extras) {
      copy.append(extra.copy());
    }

    copy.color = this.color;
    copy.insertion = this.insertion;

    copy.clickEvent = this.clickEvent;
    copy.hoverEvent = this.hoverEvent;

    return copy;
  }

  protected abstract DefaultChatComponent createCopy();

  @Override
  public void append(ChatComponent extra) {
    this.extras.add(extra);
  }

  @Override
  public boolean isEmpty() {
    for (boolean format : this.formats) {
      if (format) {
        return false;
      }
    }
    return this.color == null &&
        this.insertion == null &&
        this.clickEvent == null &&
        this.hoverEvent == null &&
        this.extras.isEmpty() &&
        this.isSpecificEmpty();
  }

  protected abstract boolean isSpecificEmpty();
}
