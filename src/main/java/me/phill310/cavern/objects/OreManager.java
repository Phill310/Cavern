package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;

/**
 * Manages the creation and deletion of ores. Moves ore data between config and CACHE
 */
public class OreManager {
    private static final LinkedHashMap<Material, Ore> ores = new LinkedHashMap<>();
    private static final Main plugin = Main.getPlugin(Main.class);

    /**
     * Load all ores from configuration file into CACHE
     */
    public static void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        FileConfiguration config = plugin.getConfig();
        ores.clear();
        for (String key: config.getConfigurationSection("ores").getKeys(false)) {
            Ore ore = new Ore(config.getConfigurationSection("ores." + key));
            ores.put(ore.getType(), ore);
        }

    }

    /**
     * Save all ores in CACHE to configuration file
     */
    public static void saveAll() {
        FileConfiguration config = plugin.getConfig();
        for (Ore ore : ores.values()) {
            config = ore.toFile(config);
        }
        plugin.saveConfig();
    }

    /**
     * Get an ore from CACHE
     * @param type The block that the ore looks like
     * @return {@link Ore} class
     */
    public static Ore getOre(Material type) {
        return ores.get(type);
    }

    /**
     * Save an ore to CACHE
     * @param ore {@link Ore} class
     */
    public static void saveOre(Ore ore) {
        ores.put(ore.getType(), ore);
    }

    /**
     * Remove an ore from CACHE
     * @param ore {@link Ore} class
     */
    public static void removeOre(Ore ore) {
        ores.remove(ore.getType());
    }

    /**
     * Remove an ore from CACHE
     * @param type The block that the ore looks like
     */
    public static void removeOre(Material type) {
        ores.remove(type);
    }

    /**
     * Check if there is an ore tied to a Material
     * @param type Material that could be an ore
     * @return true if material has an ore associated with it
     */
    public static boolean isOre(Material type) {
        return ores.containsKey(type);
    }

    /**
     * Get the number of ores in CACHE
     * @return number of ores
     */
    public static int oreCount() {
        return ores.size();
    }

    /**
     * Get an ordered list of ores from CACHE
     * @return LinkedHashMap of ore type and {@link Ore}
     */
    public static LinkedHashMap<Material, Ore> getOres() {
        return ores;
    }

}

