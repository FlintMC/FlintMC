import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.event.client.ClientTick;
import net.labyfy.component.eventbus.event.client.RenderTick;
import net.labyfy.component.eventbus.event.client.TickEvent;
import net.labyfy.component.eventbus.event.client.WorldRendererTick;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.hook.Hook;

@Singleton
@AutoLoad
public class EventBusTest {

  private final EventBus eventBus;

  @Inject
  public EventBusTest(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.Minecraft",
          methodName = "runTick"
  )
  public void hookAfterTick(Hook.ExecutionTime executionTime) {
    TickEvent event = new ClientTick();
    callTick(executionTime, event);
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.renderer.GameRenderer",
          methodName = "tick"
  )
  public void hookGameRendererTick(Hook.ExecutionTime executionTime) {
    TickEvent event = new RenderTick();
    callTick(executionTime, event);
  }

  private void callTick(Hook.ExecutionTime executionTime, TickEvent event) {
    switch (executionTime) {
      case BEFORE:
        this.eventBus.fire(event, Subscribe.Phase.PRE);
        break;
      case AFTER:
        this.eventBus.fire(event, Subscribe.Phase.POST);
        break;
    }
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.renderer.WorldRenderer",
          methodName = "tick"
  )
  public void hookWorldRendererTick(Hook.ExecutionTime executionTime) {
    TickEvent event = new WorldRendererTick();
    callTick(executionTime, event);
  }

  @Subscribe(priority = Byte.MAX_VALUE)
  public void clientTick(TickEvent event) {
    System.out.println("client tick");
  }

  @Subscribe(priority = Byte.MAX_VALUE)
  @TickEvent.TickPhase
  public void timeBasedTick(TickEvent event) {
    System.out.println("Pre client tick.");
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  @TickEvent.TickPhase(type = TickEvent.Type.RENDER)
  public void postRenderTick(TickEvent event) {
    System.out.println("Post tick renderer");
  }

  @Subscribe
  @TickEvent.TickPhase(type = TickEvent.Type.WORLD_RENDERER)
  public void preWorldRenderer(TickEvent event) {
    System.out.println("Pre tick world renderer");
  }

/*
  @Subscribe(priority = Byte.MAX_VALUE - 1, async = true)
  public void anotherTick(TickEvent event) {
    System.out.println("Async called!");
    System.out.println("After the highest tick call! Phase: " + event.getPhase().name());
  }

  @Subscribe
  public void clientTick(TickEvent event) {
    System.out.println("Tick: after Phase: " + event.getPhase().name());
  }*/
}
