package net.flintmc.render.model;

import java.util.function.Consumer;

public interface Renderable<R extends Renderable<R, T>, T> {

  T getTarget();

  RenderContext<?, R> getContext();

  Renderable<R, T> callBeforeRenderHook();

  Renderable<R, T> callAfterRenderHook();

  Renderable<R, T> callRendererNotSetAction();

  R addBeforeRenderHook(Consumer<R> consumer);

  R addAfterRenderHook(Consumer<R> consumer);

  R setRendererNotSetAction(Consumer<R> consumer);

}
