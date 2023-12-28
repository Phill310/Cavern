package me.phill310.cavern.guis.master;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public abstract class Entry {
    public Entry(Material material, String name, List<String> lore, int quantity, int slot) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.quantity = quantity;
        this.slot = slot;
    }

    public Entry(Material material, String name, List<String> lore, int quantity, int slot, boolean isGlowing) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.quantity = quantity;
        this.slot = slot;
        this.isGlowing = isGlowing;
    }

    private Material material;
    private String name;
    private List<String> lore;
    private final int quantity;
    private int slot;
    private boolean isGlowing = false;

    /**
     * Get the material of the item
     * @return material
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Set the material of the item
     * @param material new material of the item
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Get the name of the item<br>
     * Name should be a minimessage-ready string
     * @return name of item
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the item<br>
     * Should be a minimessage-ready string
     * @param name new name of item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the lore of the item<br>
     * Lore should be a list of minimessage-ready strings<br>
     * Lore will be null if there is no lore
     * @return list of lines of lore
     */
    public List<String> getLore() {
        return this.lore;
    }

    /**
     * Set the lore of the item
     * @param lore the new lore as a list of strings
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    /**
     * Get the amount of the item in this stack
     * @return amount of item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get which slot this item goes inside its gui
     * @return slot number
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Set the slot this item goes in inside its gui
     * @param slot new slot number
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Check if the item should have an enchanted glow
     * @return true if glow is present
     */
    public boolean isGlowing() {
        return isGlowing;
    }

    /**
     * Set whether the item should have an enchanted glow
     * @param glowing true if item should glow
     */
    public void setGlowing(boolean glowing) {
        isGlowing = glowing;
    }

    /**
     * Toggle whether the item should have an enchanted glow<br>
     * True -> False<br>
     * False -> True
     */
    public void toggleGlowing() {
        isGlowing = !isGlowing;
    }

    public abstract void onClick(InventoryClickEvent event);
}
