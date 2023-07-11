package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class OreManager {
    private static final HashMap<Material, Ore> ores = new HashMap<>();
    private static final Main plugin = Main.getPlugin(Main.class);

    public static void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        FileConfiguration config = plugin.getConfig();
        ores.clear();
        for (String key: config.getConfigurationSection("ores").getKeys(false)) {
            Ore ore = new Ore(config.getConfigurationSection("ores." + key));
            ores.put(ore.getType(), ore);
            Bukkit.broadcast(Component.text("Added " + ore.getType()));
            if (ore.hasDrop()) Bukkit.broadcast(Component.text("Had a drop " + ore.getDrop()));
        }

    }

    public static void saveAll() {
        FileConfiguration config = plugin.getConfig();
        for (Ore ore : ores.values()) {
            config = ore.toFile(config);
        }
        plugin.saveConfig();
    }

    public static Ore getOre(Material type) {
        return ores.get(type);
    }

    public static void saveOre(Ore ore) {
        ores.put(ore.getType(), ore);
    }
    public static void removeOre(Ore ore) {
        ores.remove(ore.getType());
    }

    public static void removeOre(Material type) {
        ores.remove(type);
    }

    public static boolean isOre(Material type) {
        return ores.containsKey(type);
    }

    public static int oreCount() {
        return ores.size();
    }

    public static HashMap<Material, Ore> getOres() {
        return ores;
    }

}

