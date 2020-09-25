import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.event.client.ClientTick;
import net.labyfy.component.eventbus.event.client.RenderTick;
import net.labyfy.component.eventbus.event.client.TickEvent;
import net.labyfy.component.eventbus.event.client.WorldRendererTick;
import net.labyfy.component.eventbus.event.entity.EntityEvent;
import net.labyfy.component.eventbus.event.entity.EntitySpawnEvent;
import net.labyfy.component.eventbus.event.util.EventPriority;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.ZombieEntity;

@Singleton
@AutoLoad
public class EventBusTest {

  private final EventBus eventBus;

  @Inject
  public EventBusTest(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  /*


  HOOKS


   */
  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "addEntity",
          parameters = {
                  @Type(reference = int.class),
                  @Type(reference = Entity.class)
          }
  )
  public void hookEntitySpawn(@Named("args") Object[] args, Hook.ExecutionTime executionTime) {
    int entityId = (int) args[0];
    Entity spawnEntity = (Entity) args[1];

    EntitySpawnEvent event = new EntitySpawnEvent(entityId, spawnEntity);
    this.eventBus.fireEvent(event, executionTime);
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE},
          className = "net.minecraft.client.Minecraft",
          methodName = "runTick"
  )
  public void hookAfterTick(Hook.ExecutionTime executionTime) {
    ClientTick event = new ClientTick();
    this.eventBus.fireEvent(event, executionTime);
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.renderer.GameRenderer",
          methodName = "tick"
  )
  public void hookGameRendererTick(Hook.ExecutionTime executionTime) {
    RenderTick event = new RenderTick();
    this.eventBus.fireEvent(event, executionTime);
  }

  @Hook(
          executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
          className = "net.minecraft.client.renderer.WorldRenderer",
          methodName = "tick"
  )
  public void hookWorldRendererTick(Hook.ExecutionTime executionTime) {
    WorldRendererTick event = new WorldRendererTick();
    this.eventBus.fireEvent(event, executionTime);
  }

  /*


  LISTENS TO EVENTS


   */

  @Subscribe(priority = Byte.MAX_VALUE, phase = Subscribe.Phase.ANY)
  public void any(TickEvent event) {
    System.out.println("any " + event.getType());
  }

  @Subscribe(priority = Byte.MAX_VALUE, phase = Subscribe.Phase.PRE)
  @TickEvent.TickPhase(type = TickEvent.Type.CLIENT)
  public void client(TickEvent event) {
    System.out.println("client " + event.getType());
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  @TickEvent.TickPhase(type = TickEvent.Type.RENDER)
  public void render(TickEvent event) {
    System.out.println("render " + event.getType());
  }

  @Subscribe
  @TickEvent.TickPhase(type = TickEvent.Type.WORLD_RENDERER)
  public void worldRender(TickEvent event) {
    System.out.println("world renderer " + event.getType());
  }

  /*
   * The ugly and bullied method.
   */
  @Subscribe
  public void handleTick(TickEvent event) {
    // Without the TickPhase annotation
    // Spigot, Forge, Fabric etc. like
    switch (event.getType()) {
      case CLIENT:
        // Handle client tick logic
        break;
      case RENDER:
        // Handle render tick logic
        break;
      case WORLD_RENDERER:
        // Handle world renderer tick logic
        break;
    }
  }

  @Subscribe(priority = EventPriority.EARLY, phase = Subscribe.Phase.POST)
  // Custom annotation for the EntityEvent group
  @EntityEvent.EntityFilter(filter = EndermanEntity.class)
  public void handleEndermanSpawn(EntitySpawnEvent event) {
    // Handle enderman spawn logic
  }

  @Subscribe(priority = EventPriority.EARLY, phase = Subscribe.Phase.PRE)
  // Custom annotation for the EntityEvent group
  @EntityEvent.EntityFilter(filter = VillagerEntity.class)
  public void handleVillagerSpawn(EntitySpawnEvent event) {
    // Handle villager spawn logic
  }

  /*
   * The ugly and bullied method.
   */
  @Subscribe(priority = 35, phase = Subscribe.Phase.POST)
  public void handleEntitySpawn(EntitySpawnEvent event) {
    // Without the EntityFilter annotation
    // Spigot, Forge, Fabric etc. like
    if (event.getEntity() instanceof ZombieEntity) {
      // Handle zombie spawn logic
    }
  }

}
