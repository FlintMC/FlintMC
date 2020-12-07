package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.event.ChatReceiveEvent;
import net.flintmc.mcapi.chat.event.ChatSendEvent;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.util.text.ITextComponent;

@Singleton
public class ChatEventInjector {

  private final EventBus eventBus;
  private final MinecraftComponentMapper componentMapper;
  private final ChatSendEvent.Factory sendFactory;
  private final ChatReceiveEvent.Factory receiveFactory;

  @Inject
  private ChatEventInjector(
      EventBus eventBus,
      MinecraftComponentMapper componentMapper,
      ChatSendEvent.Factory sendFactory,
      ChatReceiveEvent.Factory receiveFactory) {
    this.eventBus = eventBus;
    this.componentMapper = componentMapper;
    this.sendFactory = sendFactory;
    this.receiveFactory = receiveFactory;
  }

  private void addEventInjectorField(CtClass target) throws CannotCompileException {
    target.addField(
        CtField.make(
            String.format(
                "private final %s chatEventInjector = (%1$s) %s.getInjectedInstance(%1$s.class);",
                super.getClass().getName(), InjectionHolder.class.getName()),
            target));
  }

  @ClassTransform("net.minecraft.client.gui.NewChatGui")
  public void transformChatGui(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass transforming = context.getCtClass();
    this.addEventInjectorField(transforming);

    CtMethod method = transforming.getDeclaredMethod("printChatMessageWithOptionalDeletion");

    method.insertBefore(
        String.format(
            "{ $1 = this.chatEventInjector.handleChatReceive($1, %s.PRE); if ($1 == null) { return; } }",
            Subscribe.Phase.class.getName()));
    method.insertAfter(
        String.format(
            "{ this.chatEventInjector.handleChatReceive($1, %s.POST); }",
            Subscribe.Phase.class.getName()));
  }

  public ITextComponent handleChatReceive(ITextComponent component, Subscribe.Phase phase) {
    ChatComponent flintComponent = this.componentMapper.fromMinecraft(component);
    ChatReceiveEvent event = this.receiveFactory.create(flintComponent);
    this.eventBus.fireEvent(event, phase);

    if (phase != Subscribe.Phase.PRE || event.isCancelled()) {
      return null;
    }

    return (ITextComponent) this.componentMapper.toMinecraft(event.getMessage());
  }

  @ClassTransform("net.minecraft.client.entity.player.ClientPlayerEntity")
  public void transformClientPlayerEntity(ClassTransformContext context)
      throws CannotCompileException, NotFoundException {
    CtClass transforming = context.getCtClass();
    this.addEventInjectorField(transforming);

    CtMethod method = transforming.getDeclaredMethod("sendChatMessage");

    method.insertBefore(
        String.format(
            "{ $1 = this.chatEventInjector.handleChatSend($1, %s.PRE); if ($1 == null) { return; } }",
            Subscribe.Phase.class.getName()));

    method.insertAfter(
        String.format(
            " { this.chatEventInjector.handleChatSend($1, %s.POST); }",
            Subscribe.Phase.class.getName()));
  }

  public String handleChatSend(String message, Subscribe.Phase phase) {
    ChatSendEvent event = this.sendFactory.create(message);
    this.eventBus.fireEvent(event, phase);
    return event.isCancelled() ? null : event.getMessage();
  }
}
