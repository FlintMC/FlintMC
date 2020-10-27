package net.flintmc.mcapi.chat.internal.component;

import net.flintmc.mcapi.chat.component.ScoreComponent;

public class DefaultScoreComponent extends DefaultChatComponent implements ScoreComponent {

  private String name;
  private String objective;

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public void name(String name) {
    this.name = name;
  }

  @Override
  public String objective() {
    return this.objective;
  }

  @Override
  public void objective(String objective) {
    this.objective = objective;
  }

  @Override
  public String getUnformattedText() {
    return "null"; // TODO get text out of the scoreboard
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultScoreComponent component = new DefaultScoreComponent();
    component.name = this.name;
    component.objective = this.objective;
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.name == null && this.objective == null;
  }
}
