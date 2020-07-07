package net.labyfy.component.i18n;

/**
 * Localizer dispatches localization keys to the representing language text
 */
public interface Localizer {

    /**
     * Get the current String for key
     * @param key language key
     * @return String if available
     */
    String dsp(String key);

    /**
     * Reloads the labyfy lang config
     * @return true if successful
     */
    boolean reload();
}
