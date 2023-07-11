package me.phill310.cavern.objects;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Ore {
    private final Material type;
    private double cooldown;
    private int pickLevel;
    private int worth;
    private ItemStack drop;


    Ore(Material type, double cooldown, int pickLevel, int worth, ItemStack drop) {
        this.type = type;
        this.cooldown = cooldown;
        this.pickLevel = pickLevel;
        this.worth = worth;
        this.drop = drop;
    }

    Ore(ConfigurationSection config) {
        this.type = Material.getMaterial(config.getName().toUpperCase());
        this.cooldown = config.getDouble("cooldown", 10);
        this.pickLevel = config.getInt("pick-level", 1);
        this.worth = config.getInt("worth", 0);
        this.drop = config.getItemStack("drop", null);
    }

    public FileConfiguration toFile(FileConfiguration config) {
        String key = "ores." + type.name().toLowerCase() + ".";
        config.set(key + "cooldown", cooldown);
        config.set(key + "pick-level", pickLevel);
        config.set(key + "worth", worth);
        config.set(key + "drop", drop);
        return config;
    }

    public Material getType() {
        return type;
    }


    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public int getPickLevel() {
        return pickLevel;
    }

    public void setPickLevel(int pickLevel) {
        this.pickLevel = pickLevel;
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public void setDrop(ItemStack drop) {
        this.drop = drop;
    }
    public ItemStack getDrop() {
        return drop;
    }
    public boolean hasDrop() {
        return drop != null;
    }
}
