/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_15_2.debug;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;
import net.flintmc.mcapi.internal.debug.DefaultMinecraftDebugger;
import net.flintmc.render.gui.input.Key;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;

@Singleton
public class MinecraftDebuggerHooks {

  private final DefaultMinecraftDebugger debugger;

  @Inject
  private MinecraftDebuggerHooks(DefaultMinecraftDebugger debugger,
      TranslationComponentBuilder.Factory translationChatComponentBuilderFactory) {
    this.debugger = debugger;

    Map<Key, String> internalKeybindings = new HashMap<>();
    internalKeybindings.put(Key.A, "debug.reload_chunks.help");
    internalKeybindings.put(Key.B, "debug.show_hitboxes.help");
    internalKeybindings.put(Key.C, "debug.copy_location.help");
    internalKeybindings.put(Key.D, "debug.clear_chat.help");
    internalKeybindings.put(Key.F, "debug.cycle_renderdistance.help");
    internalKeybindings.put(Key.G, "debug.chunk_boundaries.help");
    internalKeybindings.put(Key.H, "debug.advanced_tooltips.help");
    internalKeybindings.put(Key.I, "debug.inspect.help");
    internalKeybindings.put(Key.N, "debug.creative_spectator.help");
    internalKeybindings.put(Key.P, "debug.pause_focus.help");
    internalKeybindings.put(Key.T, "debug.reload_resourcepacks.help");

    this.debugger.registerInternalDebugKeybindings(
        internalKeybindings
    );

    this.debugger.registerDebugKeybinding(Key.Q,
        translationChatComponentBuilderFactory.newBuilder().translationKey("debug.help.help")
            .build(),
        debugger::displayHelp);
  }

  @Hook(
      className = "net.minecraft.client.KeyboardListener",
      methodName = "processKeyF3",
      defaultValue = "true",
      executionTime = ExecutionTime.BEFORE,
      parameters = {
          @Type(reference = int.class)
      }
  )
  public HookResult hookDebugKeyHandler(@Named("args") Object[] args) {
    if (debugger.handleDebugKey(Key.getByKeyCode((int) args[0]))) {
      return HookResult.BREAK;
    }

    return HookResult.CONTINUE;
  }
}
