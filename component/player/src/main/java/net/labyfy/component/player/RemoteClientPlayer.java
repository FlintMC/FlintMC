package net.labyfy.component.player;

/**
 * Represents a remote client player
 */
public interface RemoteClientPlayer<T> extends Player<T> {

    /**
     * A factory class for {@link RemoteClientPlayer}
     *
     * @param <T> The type of this player
     */
    interface Factory<T> {

        /**
         * Creates a new {@link RemoteClientPlayer} with the given type.
         *
         * @param player A type to create this player.
         * @return a created remote client player
         */
        RemoteClientPlayer create(T player);

    }

}
