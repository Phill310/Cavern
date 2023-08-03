package me.phill310.cavern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static final MiniMessage mm = MiniMessage.miniMessage();
    public static final Main plugin = Main.getPlugin(Main.class);

    public static void give(Player player, ItemStack item) {
        final Map<Integer, ItemStack> extra = player.getInventory().addItem(item);
        if (!extra.isEmpty()) {
            extra.values().forEach(i -> player.getWorld().dropItemNaturally(player.getLocation(), i));
        }
    }

    public static boolean percentChance(double chance) {
        return Math.random() <= chance/100;
    }

    public static int randInt(double min, double max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    private static final Logger logger = Logger.getLogger("Minecraft");
    public static void log(String message) {
        log(message, Level.INFO);
    }
    public static void log(String message, Level level) {
        logger.log(level, "[Cavern] " + message);
    }

    /**
     * Easily create an item
     * @param material Material of the item
     * @param amount Amount of the item
     * @param name Display name of the item
     * @param lore Optional lines of lore for the item
     * @return A brand new ItemStack
     */
    public static ItemStack buildItem(Material material, int amount, String name, @Nullable String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize("<!i>" + name));
        if (lore != null) {
            if (lore.length > 0) {
                ArrayList<Component> loreList = new ArrayList<>();
                for (String line : lore) {
                    loreList.add(mm.deserialize("<!i>" + line));
                }
                meta.lore(loreList);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String formatDec(double value) {
        if ((int) value == value) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    //PDC
    public static ItemStack addInt(ItemStack item, String key, int value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
        return item;
    }

    public static Integer readInt(ItemStack item, String key) {
        if (item == null) return null;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (pdc.has(new NamespacedKey(plugin, key))) {
                return pdc.get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
            }
        }
        return null;
    }

    public static ItemStack addString(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    public static void addString(ItemMeta meta, String key, String value) {
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
    }

    public static String readString(ItemStack item, String key) {
        if (item == null) return null;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (pdc.has(new NamespacedKey(plugin, key))) {
                return pdc.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
            }
        }
        return null;
    }
}
