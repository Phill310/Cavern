package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import me.phill310.cavern.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
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

public class Pickaxe {
    private final Main plugin = Main.getPlugin(Main.class);
    private String owner;
    private int efficiency = 0;
    private int fortune = 0;
    private int xpBoost = 0;
    private int level = 1;
    private Material material = Material.WOODEN_PICKAXE;

    public Pickaxe(ItemStack pick) {
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

    public Pickaxe(UUID owner) {
        this.owner = owner.toString();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public void addEfficiency() {
        this.efficiency++;
    }

    public int getFortune() {
        return fortune;
    }

    public void setFortune(int fortune) {
        this.fortune = fortune;
    }

    public void addFortune() {
        this.fortune++;
    }

    public int getXpBoost() {
        return xpBoost;
    }

    public void setXpBoost(int xpBoost) {
        this.xpBoost = xpBoost;
    }

    public void addXpBoost() {
        this.xpBoost++;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel() {
        this.level++;
    }

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
