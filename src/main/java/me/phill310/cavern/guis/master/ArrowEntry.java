package me.phill310.cavern.guis.master;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ArrowEntry extends Entry {
    public ArrowEntry(Material material, String name, List<String> lore, int slot, PaginatedGUI gui) {
        super(material, name, lore, 1, slot);
        this.gui = gui;
    }

    private final PaginatedGUI gui;

    @Override
    public void onClick(InventoryClickEvent event) {
        String name = super.getName().toLowerCase();
        if (name.contains("next") || name.contains("advance")) {
            gui.nextPage(event.getClickedInventory());
        } else if (name.contains("previous") || name.contains("back")) {
            gui.previousPage(event.getClickedInventory());
        }
    }
}
