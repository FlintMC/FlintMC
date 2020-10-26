package net.labyfy.chat.builder;

import net.labyfy.chat.component.DefaultScoreComponent;
import net.labyfy.chat.component.ScoreComponent;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = ScoreComponentBuilder.class)
public class DefaultScoreComponentBuilder
    extends DefaultComponentBuilder<ScoreComponentBuilder, ScoreComponent>
    implements ScoreComponentBuilder {

  public DefaultScoreComponentBuilder() {
    super(DefaultScoreComponent::new);
  }

  @Override
  public ScoreComponentBuilder name(String name) {
    super.currentComponent.name(name);
    return this;
  }

  @Override
  public String name() {
    return super.currentComponent.name();
  }

  @Override
  public ScoreComponentBuilder objective(String objective) {
    super.currentComponent.objective(objective);
    return this;
  }

  @Override
  public String objective() {
    return super.currentComponent.objective();
  }
}
