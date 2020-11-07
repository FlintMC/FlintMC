package net.flintmc.mcapi.internal.chat.component;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.component.SelectorComponent;

import java.util.Collection;

public class DefaultSelectorComponent extends DefaultChatComponent implements SelectorComponent {

  private final Multimap<String, String> options = HashMultimap.create();
  private EntitySelector selector = EntitySelector.SELF;

  @Override
  public EntitySelector selector() {
    return this.selector;
  }

  @Override
  public void selector(EntitySelector selector) {
    this.selector = selector;
  }

  @Override
  public Multimap<String, String> selectorOptions() {
    return this.options;
  }

  @Override
  public void selectorOptions(Multimap<String, String> options) {
    this.options.clear();
    this.options.putAll(options);
  }

  @Override
  public void selectorOption(String option, String value) {
    if (value == null) {
      this.options.removeAll(option);
      return;
    }
    this.options.put(option, value);
  }

  @Override
  public Collection<String> selectorOption(String option) {
    return this.options.get(option);
  }

  @Override
  public String getUnformattedText() {
    EntitySelector selector = this.selector != null ? this.selector : EntitySelector.ALL_PLAYERS;
    StringBuilder builder = new StringBuilder();
    builder.append(selector.getShortcut());

    if (!this.options.isEmpty()) {
      builder.append('[');

      StringBuilder optionBuilder = new StringBuilder();
      this.options.forEach(
          (key, value) -> optionBuilder.append(key).append('=').append(value).append(','));
      builder.append(
          optionBuilder.length() == 0
              ? ""
              : optionBuilder.substring(0, optionBuilder.length() - 1));

      builder.append(']');
    }

    return builder.toString();
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultSelectorComponent component = new DefaultSelectorComponent();
    component.selector = this.selector;
    this.options.forEach(component.options::put);
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.selector == null && this.options.isEmpty();
  }
}
