package net.labyfy.internal.player.v1_15_2.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.player.gui.TabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/**
 * 1.15.2 implementation of {@link TabOverlay}
 */
@Singleton
@Implement(TabOverlay.class)
public class VersionedTabOverlay implements TabOverlay {

    private final ClassMappingProvider classMappingProvider;

    @Inject
    public VersionedTabOverlay(ClassMappingProvider classMappingProvider) {
        this.classMappingProvider = classMappingProvider;
    }

    /**
     * Retrieves the header of this player.
     *
     * @return the header of this player
     */
    @Override
    public Object getHeader() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        return this.classMappingProvider
                .get("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
                .getField("header")
                .getValue(Minecraft.getInstance().ingameGUI.getTabList());
    }

    /**
     * Updates the header of this player.
     *
     * @param header The new header content
     */
    @Override
    public void updateHeader(Object header) {
        Minecraft.getInstance().ingameGUI.getTabList().setFooter((ITextComponent) header);
    }

    /**
     * Retrieves the footer of this player.
     *
     * @return the footer of this player.
     */
    @Override
    public Object getFooter() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        return this.classMappingProvider
                .get("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
                .getField("footer")
                .getValue(Minecraft.getInstance().ingameGUI.getTabList());
    }

    /**
     * Updates the footer of this player.
     *
     * @param footer The new footer content
     */
    @Override
    public void updateFooter(Object footer) {
        Minecraft.getInstance().ingameGUI.getTabList().setFooter((ITextComponent) footer);
    }
}
