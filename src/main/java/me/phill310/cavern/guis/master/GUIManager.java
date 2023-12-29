package me.phill310.cavern.guis.master;

import me.phill310.cavern.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Open and update guis
 */
public class GUIManager {
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final Main plugin = Main.getPlugin(Main.class);

    /**
     * Open a gui to a player
     * @param player player being shown gui
     * @param gui gui to be shown
     */
    public void openGUI(Player player, GUI gui) {
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(gui), plugin);
        player.openInventory(setupInventory(gui));
    }

    /***
     * Update an open inventory by setting all the slots to their entry again
     * @param inventory inventory being updated
     * @param gui gui to get entries from
     */
    public void updateGUI(Inventory inventory, GUI gui) {
        for (Entry entry : gui.getEntries().values()) {
            if (entry.getSlot() < gui.getSize()) inventory.setItem(entry.getSlot(), buildEntry(entry));
        }
        gui.setInventory(inventory);
    }

    /**
     * Create and populate an inventory using data from a gui
     * @param gui gui to get items from
     * @return populated inventory
     */
    private Inventory setupInventory(GUI gui) {
        Inventory inventory = Bukkit.createInventory(null, gui.getSize(), gui.getTitle());

        for (Entry entry : gui.getEntries().values()) {
            if (entry.getSlot() < gui.getSize()) inventory.setItem(entry.getSlot(), buildEntry(entry));
        }

        gui.setInventory(inventory);
        return inventory;
    }

    /**
     * Create an item from an entry
     * @param entry entry to be converted
     * @return item representation of entry
     */
    private ItemStack buildEntry(Entry entry) {
        ItemStack item = new ItemStack(entry.getMaterial(), entry.getQuantity());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize("<!i>" + entry.getName()));
        if (entry.getLore() != null) {
            List<Component> lore = new ArrayList<>();
            for (String line : entry.getLore()) {
                lore.add(mm.deserialize("<!i>" + line));
            }
            meta.lore(lore);
        }
        if (entry.isGlowing()) {
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }
}
