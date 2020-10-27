package net.flintmc.mcapi.chat.internal.builder;

import net.flintmc.mcapi.chat.builder.ScoreComponentBuilder;
import net.flintmc.mcapi.chat.component.ScoreComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.internal.component.DefaultScoreComponent;

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
