package net.labyfy.internal.webgui.ultralight.event.interop;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.internal.webgui.ultralight.event.*;
import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewLoadingEvent;
import net.labymedia.ultralight.UltralightView;
import net.labymedia.ultralight.input.UltralightCursor;
import net.labymedia.ultralight.math.IntRect;
import net.labymedia.ultralight.plugin.loading.UltralightLoadListener;
import net.labymedia.ultralight.plugin.view.MessageLevel;
import net.labymedia.ultralight.plugin.view.MessageSource;
import net.labymedia.ultralight.plugin.view.UltralightViewListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Interop for Ultralight to Labyfy {@link net.labyfy.webgui.event.WebGuiViewEvent}s.
 */
public class UltralightWebGuiViewEventInterop implements UltralightViewListener, UltralightLoadListener {
  private final WebGuiView view;
  private final EventBus eventBus;
  private final Logger logger;

  @AssistedInject
  private UltralightWebGuiViewEventInterop(@Assisted WebGuiView view, EventBus eventBus, @InjectLogger Logger logger) {
    this.view = view;
    this.eventBus = eventBus;
    this.logger = logger;
  }

  @Override
  public void onChangeTitle(String title) {
    eventBus.fireEvent(new UltralightWebGuiViewTitleChangedEvent(view, title), Subscribe.Phase.POST);
  }

  @Override
  public void onChangeURL(String url) {
    eventBus.fireEvent(new UltralightWebGuiViewURLChangedEvent(view, url), Subscribe.Phase.POST);
  }

  @Override
  public void onChangeTooltip(String tooltip) {
    eventBus.fireEvent(new UltralightWebGuiViewTitleChangedEvent(view, tooltip), Subscribe.Phase.POST);
  }

  @Override
  public void onChangeCursor(UltralightCursor cursor) {

  }

  @Override
  public void onAddConsoleMessage(
      MessageSource source, MessageLevel level, String message, long lineNumber, long columnNumber, String sourceId) {
    Level logLevel;
    switch(level) {
      case DEBUG:
        logLevel = Level.DEBUG;
        break;

      case LOG:
      case INFO:
        logLevel = Level.INFO;
        break;

      case WARNING:
        logLevel = Level.WARN;
        break;

      case ERROR:
        logLevel = Level.ERROR;
        break;

      default:
        logLevel = Level.ALL;
        break;
    }

    logger.log(logLevel, "({} in {}:{}:{}): {}", source.name(), source, lineNumber, columnNumber, message);
  }

  @Override
  public UltralightView onCreateChildView(String openerUrl, String targetUrl, boolean isPopup, IntRect popupRect) {
    return null;
  }

  @Override
  public void onBeginLoading(long frameId, boolean isMainFrame, String url) {
    eventBus.fireEvent(
        new UltralightWebGuiViewLoadingEvent(view, frameId, url, isMainFrame, null), Subscribe.Phase.PRE);
  }

  @Override
  public void onFinishLoading(long frameId, boolean isMainFrame, String url) {
    eventBus.fireEvent(
        new UltralightWebGuiViewLoadingEvent(view, frameId, url, isMainFrame, null), Subscribe.Phase.POST);
  }

  @Override
  public void onFailLoading(
      long frameId, boolean isMainFrame, String url, String description, String errorDomain, int errorCode) {
    eventBus.fireEvent(
        new UltralightWebGuiViewLoadingEvent(
            view,
            frameId,
            url,
            isMainFrame,
            new WebGuiViewLoadingEvent.ErrorInfo(description, errorDomain, errorCode)
        ), Subscribe.Phase.POST);
  }

  @Override
  public void onUpdateHistory() {
    eventBus.fireEvent(new UltralightWebGuiViewNavigateEvent(view), Subscribe.Phase.POST);
  }

  @Override
  public void onWindowObjectReady(long frameId, boolean isMainFrame, String url) {
    eventBus.fireEvent(
        new UltralightWebGuiViewWindowObjectReadyEvent(view, frameId, url, isMainFrame), Subscribe.Phase.POST);
  }

  @Override
  public void onDOMReady(long frameId, boolean isMainFrame, String url) {
    eventBus.fireEvent(
        new UltralightWebGuiViewDOMReadyEvent(view, frameId, url, isMainFrame), Subscribe.Phase.POST);
  }

  /**
   * Sends an event that the view is closing.
   *
   * @param phase The phase of the event
   */
  public void notifyClose(Subscribe.Phase phase) {
    eventBus.fireEvent(new UltralightWebGuiViewCloseEvent(view), phase);
  }

  /**
   * Assisted factory for {@link UltralightWebGuiViewEventInterop} instances.
   */
  @AssistedFactory(UltralightWebGuiViewEventInterop.class)
  public interface Factory {
    /**
     * Creates a new {@link UltralightWebGuiViewEventInterop} instance for the given view.
     *
     * @param view The view to create the instance for
     * @return The created instance
     */
    UltralightWebGuiViewEventInterop create(WebGuiView view);
  }
}
