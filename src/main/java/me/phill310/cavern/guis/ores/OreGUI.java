package me.phill310.cavern.guis.ores;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.guis.master.GUIManager;
import me.phill310.cavern.guis.master.PaginatedGUI;
import me.phill310.cavern.objects.Ore;
import me.phill310.cavern.objects.OreManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class OreGUI extends PaginatedGUI {

    public OreGUI(String identifier, Component title, GUIManager manager) {
        super(identifier, title, manager);
        for (Ore ore : OreManager.getOres().values()) {
            addEntry(new OreEntry(ore, this));
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        for (Entry entry : super.getAllEntries()) {
            if (entry instanceof OreEntry) {
                ((OreEntry) entry).setup(player);
            }
        }
        super.generatePage();
        super.loadPage(event.getInventory());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    public void update(Inventory inventory) {
        super.generatePage();
        super.loadPage(inventory);
    }
}
