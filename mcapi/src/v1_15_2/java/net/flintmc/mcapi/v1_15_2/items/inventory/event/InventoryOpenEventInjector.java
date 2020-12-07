package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryOpenEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;

@Singleton
public class InventoryOpenEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final InventoryOpenEvent.Factory eventFactory;

  @Inject
  public InventoryOpenEventInjector(EventBus eventBus, InventoryController controller, InventoryOpenEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.gui.ScreenManager$IScreenFactory",
      methodName = "createScreen",
      parameters = {
        @Type(typeName = "net.minecraft.util.text.ITextComponent"),
        @Type(typeName = "net.minecraft.inventory.container.ContainerType"),
        @Type(typeName = "net.minecraft.client.Minecraft"),
        @Type(reference = int.class)
      })
  public void createScreen() {
    InventoryOpenEvent event = this.eventFactory.create(this.controller.getOpenInventory());
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  @Hook(
          className = "net.minecraft.client.Minecraft",
          methodName = "displayGuiScreen",
          parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
          executionTime = Hook.ExecutionTime.AFTER,
          version = "1.15.2")
  public void displayGuiScreen(@Named("args") Object[] args) {
    if (!(args[0] instanceof InventoryScreen)) {
      return;
    }

    InventoryOpenEvent event = this.eventFactory.create(this.controller.getPlayerInventory());
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

}
