package me.phill310.cavern.guis.master;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * An implementation of the Entry class used to create borders and fill the gui
 */
public class GlassEntry extends Entry {
    public GlassEntry(int slot) {
        super(Material.BLACK_STAINED_GLASS_PANE, "", null, 1, slot);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
    }
}
