package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.network.NetworkPlayerInfoSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * 1.15.2 implementation of {@link NetworkPlayerInfoRegistry}
 */
@Implement(value = NetworkPlayerInfoRegistry.class, version = "1.15.2")
public class VersionedNetworkPlayerInfoRegistry implements NetworkPlayerInfoRegistry {

    private final ClientPlayNetHandler connection;
    private final NetworkPlayerInfoSerializer<net.minecraft.client.network.play.NetworkPlayerInfo> networkPlayerInfoSerializer;

    @Inject
    public VersionedNetworkPlayerInfoRegistry(
            NetworkPlayerInfoSerializer networkPlayerInfoSerializer
    ) {
        this.connection = Minecraft.getInstance().getConnection();
        this.networkPlayerInfoSerializer = networkPlayerInfoSerializer;
    }

    /**
     * Retrieves the network info of a player with the username
     *
     * @param username The username of a player
     * @return the network info of a player
     */
    @Override
    public NetworkPlayerInfo getPlayerInfo(String username) {
        return this.networkPlayerInfoSerializer.deserialize(this.connection.getPlayerInfo(username));
    }

    /**
     * Retrieves the network info of a player with the unique identifier
     *
     * @param uniqueId The unique identifier of a player
     * @return the network info of a player
     */
    @Override
    public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        return this.networkPlayerInfoSerializer.deserialize(this.connection.getPlayerInfo(uniqueId));
    }

    /**
     * Retrieves a collection of a network player info
     *
     * @return a collection of {@link NetworkPlayerInfo}
     */
    @Override
    public Collection<NetworkPlayerInfo> getPlayerInfo() {
        Collection<NetworkPlayerInfo> collection = new ArrayList<>();
        for (net.minecraft.client.network.play.NetworkPlayerInfo playerInfo : this.connection.getPlayerInfoMap()) {
            NetworkPlayerInfo networkPlayerInfo = this.networkPlayerInfoSerializer.deserialize(playerInfo);
            System.out.println(networkPlayerInfo);
            collection.add(networkPlayerInfo);
        }

        return collection;
    }

}
