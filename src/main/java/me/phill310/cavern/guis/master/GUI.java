package me.phill310.cavern.guis.master;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public abstract class GUI {
    public GUI(String identifier, int size, Component title) {
        this.identifier = identifier;
        this.size = size;
        this.title = title;
    }

    private final String identifier;
    private final int size;
    private final Component title;
    private Inventory inventory;
    private final HashMap<Integer, Entry> entries = new HashMap<>();

    /**
     * Get the identifier of the gui
     * @return string identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Get the size of the gui. Should be a multiple of 9
     * @return size of gui
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Get the title of the gui
     * @return title as a component
     */
    public Component getTitle() {
        return this.title;
    }

    /**
     * Get the inventory that represents the gui
     * @return inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Set the inventory that represents the gui
     * @param inventory inventory that represents the gui
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Get a map of the items in the gui
     * @return map of gui items
     */
    public HashMap<Integer, Entry> getEntries() {
        return entries;
    }

    /**
     * Add an entry (item) to the gui
     * @param entry entry to be added
     */
    public void addEntry(Entry entry) {
        entries.put(entry.getSlot(), entry);
    }

    /**
     * Remove an entry (item) from the gui
     * @param entry entry to be removed
     */
    public void removeEntry(Entry entry) {
        if (entries.containsValue(entry)) entries.remove(entry.getSlot(), entry);
    }

    /**
     * Creates a border for the gui out of black stained glass panes
     */
    public void createBorder() {
        if (size > 27) {
            for (int i = 0; i < 9; i++) {
                entries.put(i, new GlassEntry(i));
                entries.put(size-1-i, new GlassEntry(size-1-i));
            }
            for (int i = 1; i < ((size / 9) - 1); i++) {
                entries.put(i*9, new GlassEntry(i*9));
                entries.put(i*9+8, new GlassEntry(i*9+8));
            }
        }
    }

    /**
     * Fills all empty slots in the gui with black stained glass panes
     */
    public void fill() {
        for (int i = 0; i < size; i++) {
            if (!entries.containsKey(i)) entries.put(i, new GlassEntry(i));
        }
    }
    public abstract void onOpen(InventoryOpenEvent event);
    public abstract void onClose(InventoryCloseEvent event);
}
