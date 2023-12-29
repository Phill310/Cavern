package me.phill310.cavern.guis.master;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Manages gui events, passing them to the correct items
 */
public class GUIListener implements Listener {
    private final GUI gui;

    public GUIListener(GUI gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(gui.getInventory())) return;
        event.setCancelled(true);
        if (gui.getEntries().containsKey(event.getRawSlot())) {
            Entry entry = gui.getEntries().get(event.getRawSlot());
            entry.onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(gui.getInventory())) return;
        gui.onClose(event);
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!event.getInventory().equals(gui.getInventory())) return;
        gui.onOpen(event);
    }
}
