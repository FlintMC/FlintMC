package net.flintmc.render.minecraft.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.minecraft.image.ImageFullRenderBuilder;
import net.flintmc.render.minecraft.text.raw.FontRenderBuilder;

/**
 * This event will be fired whenever the screen is rendered. It will be fired in both {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases and it will always be fired on the
 * render thread by Minecraft, which means that in this event for example the {@link FontRenderBuilder} and {@link
 * ImageFullRenderBuilder} can be used to draw objects.
 *
 * @see Subscribe
 */
public interface ScreenRenderEvent extends Event {}
