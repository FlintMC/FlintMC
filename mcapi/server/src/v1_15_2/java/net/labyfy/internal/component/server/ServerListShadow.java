package net.labyfy.internal.component.server;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;
import net.minecraft.client.multiplayer.ServerData;

import java.util.List;

@Shadow("net.minecraft.client.multiplayer.ServerList")
public interface ServerListShadow {

    @FieldGetter("servers")
    List<ServerData> getServerDataList();

}
