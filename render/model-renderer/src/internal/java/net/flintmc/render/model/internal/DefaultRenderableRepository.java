package net.flintmc.render.model.internal;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.model.RenderableRepository;
import net.flintmc.render.model.Renderable;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(RenderableRepository.class)
public class DefaultRenderableRepository implements RenderableRepository {

  private final Map<Object, Renderable<?, ?>> renderContextMap = new HashMap<>();

  public RenderableRepository register(Renderable<?, ?> renderable) {
    this.renderContextMap.put(renderable.getTarget(), renderable);
    return this;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public RenderableRepository render(Object target) {
    Renderable renderable = this.renderContextMap.get(target);
    if (renderable != null) {
      renderable
          .callBeforeRenderHook();

      if (renderable.getContext().getRenderer() == null) {
        renderable.callRendererNotSetAction();
      } else {
        renderable
            .getContext()
            .getRenderer()
            .render(renderable, renderable.getContext());
      }

      renderable
          .callAfterRenderHook();
    }
    return this;
  }
}
