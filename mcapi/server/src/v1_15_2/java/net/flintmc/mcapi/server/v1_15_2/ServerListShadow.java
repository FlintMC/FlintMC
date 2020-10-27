package net.flintmc.mcapi.server.v1_15_2;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.multiplayer.ServerData;

import java.util.List;

@Shadow("net.minecraft.client.multiplayer.ServerList")
public interface ServerListShadow {

    @FieldGetter("servers")
    List<ServerData> getServerDataList();

}
