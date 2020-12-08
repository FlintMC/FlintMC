package net.flintmc.mcapi.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.event.TabHeaderFooterUpdateEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;

@Singleton
public class VersionedTabHeaderFooterEventInjector {

  private final EventBus eventBus;
  private final MinecraftComponentMapper componentMapper;
  private final TabHeaderFooterUpdateEvent.Factory eventFactory;

  @Inject
  private VersionedTabHeaderFooterEventInjector(
      EventBus eventBus,
      MinecraftComponentMapper componentMapper,
      TabHeaderFooterUpdateEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.componentMapper = componentMapper;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "setHeader",
      parameters = @Type(typeName = "net.minecraft.util.text.ITextComponent"),
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER})
  public HookResult handleHeader(@Named("args") Object[] args, ExecutionTime executionTime) {
    Object newHeader = args[0];

    return this.fire(newHeader, TabHeaderFooterUpdateEvent.Type.HEADER, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "setFooter",
      parameters = @Type(typeName = "net.minecraft.util.text.ITextComponent"),
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER})
  public HookResult handleFooter(@Named("args") Object[] args, ExecutionTime executionTime) {
    Object newFooter = args[0];

    return this.fire(newFooter, TabHeaderFooterUpdateEvent.Type.FOOTER, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.gui.overlay.PlayerTabOverlayGui",
      methodName = "resetFooterHeader",
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER})
  public HookResult handleReset(ExecutionTime executionTime) {
    TabHeaderFooterUpdateEvent headerEvent =
        this.eventFactory.create(null, TabHeaderFooterUpdateEvent.Type.HEADER);
    TabHeaderFooterUpdateEvent footerEvent =
        this.eventFactory.create(null, TabHeaderFooterUpdateEvent.Type.HEADER);

    boolean cancelled =
        this.eventBus.fireEvent(headerEvent, executionTime).isCancelled()
            || this.eventBus.fireEvent(footerEvent, executionTime).isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }

  private HookResult fire(
      Object newValue, TabHeaderFooterUpdateEvent.Type type, ExecutionTime executionTime) {

    TabHeaderFooterUpdateEvent event =
        this.eventFactory.create(
            newValue == null ? null : this.componentMapper.fromMinecraft(newValue), type);
    boolean cancelled = this.eventBus.fireEvent(event, executionTime).isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }
}
