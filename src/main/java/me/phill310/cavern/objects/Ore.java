package me.phill310.cavern.objects;

import me.phill310.cavern.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the necessary information about an ore including:<br>
 * - {@link Ore#getType()}<br>
 * - {@link Ore#getCooldown()}<br>
 * - {@link Ore#getPickLevel()}<br>
 * - {@link Ore#getWorth()}<br>
 * - {@link Ore#getDrop()}
 */
public class Ore {
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final Material type;
    private double cooldown;
    private int pickLevel;
    private double worth;
    private ItemStack drop;
    private ItemStack formattedDrop;

    /**
     * Create an ore, explicitly providing characteristics
     * @param type The block that the ore will look like
     * @param cooldown How long it will take the ore to respawn (in seconds)
     * @param pickLevel What level pickaxe is required to break it
     * @param worth How much the drop will be worth
     * @param drop What item the ore will drop
     */
    Ore(Material type, double cooldown, int pickLevel, double worth, ItemStack drop) {
        this.type = type;
        this.cooldown = cooldown;
        this.pickLevel = pickLevel;
        this.worth = worth;
        this.drop = drop;
        formatDrop();
    }

    /**
     * Create an ore from a configuration section
     * @param config The configuration section with the name being the ore
     */
    Ore(ConfigurationSection config) {
        this.type = Material.getMaterial(config.getName().toUpperCase());
        this.cooldown = config.getDouble("cooldown", 10);
        this.pickLevel = config.getInt("pick-level", 1);
        this.worth = config.getDouble("worth", 0);
        this.drop = config.getItemStack("drop", null);
        formatDrop();
    }

    /**
     * Save all the ore's data as a configuration section with a configuration file
     * @param config The configuration file you are saving to
     * @return configuration file with the ore's data added
     */
    public FileConfiguration toFile(FileConfiguration config) {
        String key = "ores." + type.name().toLowerCase() + ".";
        config.set(key + "cooldown", cooldown);
        config.set(key + "pick-level", pickLevel);
        config.set(key + "worth", worth);
        config.set(key + "drop", drop);
        return config;
    }

    /**
     * Get the block that the ore looks like
     * @return material of the block of the ore
     */
    public Material getType() {
        return type;
    }

    /**
     * Get how long it takes for the ore to respawn
     * @return time it takes to respawn (in seconds)
     */
    public double getCooldown() {
        return cooldown;
    }

    /**
     * Set how long it takes the ore to respawn
     * @param cooldown time it takes to respawn in seconds
     */
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Get what level pickaxe you need to break this ore
     * @return pickaxe level
     */
    public int getPickLevel() {
        return pickLevel;
    }

    /**
     * Set what level pickaxe a player needs to break this ore
     * @param pickLevel pickaxe level
     */
    public void setPickLevel(int pickLevel) {
        this.pickLevel = pickLevel;
    }

    /**
     * Get the base price of the drop of this ore
     * @return worth of drop
     */
    public double getWorth() {
        return worth;
    }

    /**
     * Set the base price of the drop of this ore
     * @param worth new worth of the drop
     */
    public void setWorth(double worth) {
        this.worth = worth;
        formatDrop();
    }

    /**
     * Set the item that gets dropped when the ore is mined
     * @param drop ItemStack to drop
     */
    public void setDrop(ItemStack drop) {
        if (drop == null) {
            this.drop = null;
        } else {
            this.drop = drop.clone();
        }
        formatDrop();
    }

    /**
     * Add the worth to the lore and PDC of the drop
     */
    private void formatDrop() {
        if (drop == null) {
            this.formattedDrop = null;
            return;
        }
        ItemStack item = drop.clone();
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(mm.deserialize("<!i><gray>Worth: <green>$" + Utils.formatDec(worth)));
        item.lore(lore);
        this.formattedDrop = Utils.addDouble(item, "worth", worth);
    }

    /**
     * Check if the ore has a drop set
     * @return true if drop is not null
     */
    public boolean hasDrop() {
        return drop != null;
    }

    /**
     * Get the item the ore will drop (with worth)
     * @return ItemStack that the ore should drop
     */
    public ItemStack getDrop() {
        return formattedDrop;
    }
}
