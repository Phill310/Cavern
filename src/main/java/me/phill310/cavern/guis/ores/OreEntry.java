package me.phill310.cavern.guis.ores;

import me.phill310.cavern.Utils;
import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.objects.Ore;
import me.phill310.cavern.objects.OreManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class OreEntry extends Entry {
    public OreEntry(Ore ore, OreGUI gui) {
        super(Material.STONE, "Placeholder", null, 1, 0);
        this.ore = ore;
        this.gui = gui;
    }

    private final Ore ore;
    private final OreGUI gui;
    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasPermission("cavern.admin")) {
            if (event.isLeftClick()) {
                gui.removeEntry(this);
                OreManager.removeOre(ore);
                gui.update(event.getClickedInventory());
            } else {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")) + " will not drop anything."));
                    ore.setDrop(null);
                } else {
                    ore.setDrop(player.getInventory().getItemInMainHand());
                    player.sendMessage(MiniMessage.miniMessage().deserialize(Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")) + " will now drop " + ore.getDrop().getType().toString().toLowerCase().replace("_", " ")));
                }
                OreManager.saveOre(ore);
            }
        }
    }

    public void setup(Player player) {
        super.setMaterial(ore.getType());
        super.setName(Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")));
        List<String> lore = new ArrayList<>();
        lore.add("<yellow>Respawn time: <white>" + Utils.formatDec(ore.getCooldown()) + "s");
        lore.add("<yellow>Pickaxe Level: <white>" + ore.getPickLevel());
        lore.add("<yellow>Worth: <green>$" + ore.getWorth());
        if (player.hasPermission("cavern.admin")) {
            lore.add("<reset>");
            lore.add("<aqua>Right-Click to set drop!");
            lore.add("<red>Left-Click to remove!");
        }
        super.setLore(lore);
    }
}
