package net.labyfy.component.player.overlay;

/**
 * Represents the tab overlay
 */
public interface TabOverlay {

    /**
     * Retrieves the header of this player.
     *
     * @return the header of this player
     */
    // TODO: 02.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    Object getHeader() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

    /**
     * Updates the header of this player.
     *
     * @param header The new header content
     */
    // TODO: 02.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    void updateHeader(Object header);

    /**
     * Retrieves the footer of this player.
     *
     * @return the footer of this player.
     */
    // TODO: 02.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    Object getFooter() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

    /**
     * Updates the footer of this player.
     *
     * @param footer The new footer content
     */
    // TODO: 02.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    void updateFooter(Object footer);

}
