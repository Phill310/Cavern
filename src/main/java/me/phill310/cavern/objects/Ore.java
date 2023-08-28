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

public class Ore {
    private final MiniMessage mm = MiniMessage.miniMessage();
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
        ItemStack item = drop.clone();
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(mm.deserialize("<!i><gray>Worth: <green>$" + worth));
        item.lore(lore);
        return Utils.addInt(item, "worth", worth);
    }
    public boolean hasDrop() {
        return drop != null;
    }
}
