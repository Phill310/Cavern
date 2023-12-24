package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import me.phill310.cavern.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Reads and modifies all info from a pickaxe including:<br>
 * - {@link Pickaxe#getOwner()}<br>
 * - {@link Pickaxe#getEfficiency()}<br>
 * - {@link Pickaxe#getFortune()}<br>
 * - {@link Pickaxe#getXpBoost()}<br>
 * - {@link Pickaxe#getLevel()}
 */
public class Pickaxe {
    private final Main plugin = Main.getPlugin(Main.class);
    private String owner;
    private int efficiency = 0;
    private int fortune = 0;
    private int xpBoost = 0;
    private int level = 1;
    private Material material = Material.WOODEN_PICKAXE;
    private boolean isPick = false;

    /**
     * Import data from a pre-existing pickaxe
     * @param pick item being read
     */
    public Pickaxe(ItemStack pick) {
        if (!List.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE).contains(pick.getType())) return;
        isPick = true;
        if (!pick.hasItemMeta()) return;
        ItemMeta meta = pick.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        owner = pdc.get(new NamespacedKey(plugin, "owner"), PersistentDataType.STRING);
        if (pdc.has(new NamespacedKey(plugin, "efficiency"), PersistentDataType.INTEGER)) efficiency = pdc.get(new NamespacedKey(plugin, "efficiency"), PersistentDataType.INTEGER);
        if (pdc.has(new NamespacedKey(plugin, "fortune"), PersistentDataType.INTEGER)) fortune = pdc.get(new NamespacedKey(plugin, "fortune"), PersistentDataType.INTEGER);
        if (pdc.has(new NamespacedKey(plugin, "xpboost"), PersistentDataType.INTEGER)) xpBoost = pdc.get(new NamespacedKey(plugin, "xpboost"), PersistentDataType.INTEGER);
        if (pdc.has(new NamespacedKey(plugin, "level"), PersistentDataType.INTEGER)) level = pdc.get(new NamespacedKey(plugin, "level"), PersistentDataType.INTEGER);
        material = pick.getType();
    }

    /**
     * Create a new pickaxe
     * @param owner UUID of the owner of this pickaxe
     */
    public Pickaxe(UUID owner) {
        this.owner = owner.toString();
        isPick = true;
    }

    /**
     * Get the owner of this pickaxe
     * @return UUID of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the owner of this pickaxe
     * @param owner UUID of the new owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the level of efficiency of this pickaxe
     * @return level of efficiency
     */
    public int getEfficiency() {
        return efficiency;
    }

    /**
     * Set the level of efficiency of this pickaxe
     * @param efficiency new level of efficiency
     */
    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    /**
     * Add 1 to the level of efficiency of this pickaxe
     */
    public void addEfficiency() {
        this.efficiency++;
    }

    /**
     * Get the level of fortune of this pickaxe
     * @return level of fortune
     */
    public int getFortune() {
        return fortune;
    }

    /**
     * Set the level of fortune of this pickaxe
     * @param fortune new level of fortune
     */
    public void setFortune(int fortune) {
        this.fortune = fortune;
    }

    /**
     * Add 1 to the level of fortune of this pickaxe
     */
    public void addFortune() {
        this.fortune++;
    }

    /**
     * Get the level of xp boost of this pickaxe
     * @return level of xp boost
     */
    public int getXpBoost() {
        return xpBoost;
    }

    /**
     * Set the level of xp boost of this pickaxe
     * @param xpBoost new level of xp boost
     */
    public void setXpBoost(int xpBoost) {
        this.xpBoost = xpBoost;
    }

    /**
     * Add 1 to the level of xp boost of this pickaxe
     */
    public void addXpBoost() {
        this.xpBoost++;
    }

    /**
     * Get the level of this pickaxe<br>
     * Used to determine which ores the pickaxe can break
     * @return level of pickaxe
     * @see Ore#getPickLevel()
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the level of this pickaxe<br>
     * Used to determine which ores the pickaxe can break
     * @param level new level of the pickaxe
     * @see Ore#getPickLevel()
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Add 1 to the level of this pickaxe<br>
     * Used to determine which ores the pickaxe can break
     * @see Ore#getPickLevel()
     */
    public void addLevel() {
        this.level++;
    }

    /**
     * Check if the item that was read in {@link #Pickaxe} was a pickaxe
     * @return true if read item was a pickaxe
     */
    public boolean isPick() {
        return isPick;
    }

    /**
     * Create a pickaxe ItemStack with all data managed by {@link Pickaxe}
     * @return pickaxe item
     */
    public ItemStack getPick() {
        ItemStack pick = Utils.buildItem(material, 1,"<gold>Miner's Pickaxe <gray>[Tier " + level + "]");
        ItemMeta meta = pick.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "owner"), PersistentDataType.STRING, owner);
        pdc.set(new NamespacedKey(plugin, "efficiency"), PersistentDataType.INTEGER, efficiency);
        pdc.set(new NamespacedKey(plugin, "fortune"), PersistentDataType.INTEGER, fortune);
        pdc.set(new NamespacedKey(plugin, "xpboost"), PersistentDataType.INTEGER, xpBoost);
        pdc.set(new NamespacedKey(plugin, "level"), PersistentDataType.INTEGER, level);
        if (efficiency > 0) meta.addEnchant(Enchantment.DIG_SPEED, efficiency, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        MiniMessage mm = MiniMessage.miniMessage();
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<!i><b><white>Enchants"));
        if (efficiency > 0) lore.add(mm.deserialize("<!i><gray>Efficiency " + efficiency));
        if (fortune > 0) lore.add(mm.deserialize("<!i><gray>Fortune " + fortune));
        if (xpBoost > 0) lore.add(mm.deserialize("<!i><gray>Xp Boost " + xpBoost));
        meta.lore(lore);
        pick.setItemMeta(meta);
        return pick;
    }
}
