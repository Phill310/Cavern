package me.phill310.cavern.mine.commands;

import me.phill310.cavern.Utils;
import me.phill310.cavern.objects.Ore;
import me.phill310.cavern.objects.OreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandOres implements TabExecutor, Listener {
    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length > 0) {
                open(player, Integer.parseInt(args[0])-1);
            } else {
                open(player);
            }
        } else {
            commandSender.sendMessage(Component.text("That aint gonna work chief"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    private void open(Player player) {
        open(player, 0);
    }
    private void open(Player player, int page) {
        int size = (int) ((Math.ceil(((double) OreManager.oreCount())/9d)+1))*9;
        boolean next = false;
        if (size >= 54) {
            size = 54;
            next = true;
        }
        Inventory inv = Bukkit.createInventory(null, size, mm.deserialize("<blue><b>Ore Info" + ((page != 0) ? " (Page " + page + ")" : "")));
        for (Ore ore : OreManager.getOres().values()) {
            ItemStack item = Utils.buildItem(ore.getType(), 1, "<aqua><b>" + Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")), "<yellow>Respawn time: <white>" + Utils.formatDec(ore.getCooldown()) + "s", "<yellow>Pickaxe Level: <white>" + ore.getPickLevel(), "<yellow>Worth: <green>$" + ore.getWorth());
            if (player.hasPermission("cavern.admin")) {
                ItemMeta meta = item.getItemMeta();
                List<Component> lore = meta.lore();
                if (lore == null) lore = new ArrayList<>();
                lore.add(Component.space());
                lore.add(mm.deserialize("<!i><aqua>Right-Click to set drop!"));
                lore.add(mm.deserialize("<!i><red>Left-Click to remove!"));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            inv.addItem(Utils.addInt(Utils.addString(item, "gen", "demoGen"), "current", page));
        }
        ItemStack glass = Utils.addString(Utils.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, ""), "inv", "ore");
        for (int i = size-9; i < size; i++) {
            inv.setItem(i, glass);
        }
        if (page > 0) inv.setItem(size-8, Utils.addInt(Utils.buildItem(Material.ARROW, 1, "<blue>Back to page " + (page-1)), "page", page-1));
        if (next) {
            inv.setItem(52, Utils.addInt(Utils.buildItem(Material.ARROW, 1, "<blue>Back to page " + (page+1)), "page", page+1));
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;

        ItemStack testItem = inv.getItem(inv.getSize()-8);
        if (testItem == null) return;
        if ((Utils.readString(testItem, "inv") == null) || (!Utils.readString(testItem, "inv").equals("ore"))) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (player.hasPermission("cavern.admin")) {
            if (Utils.readString(item, "gen").equals("demoGen")) {
                if (event.isLeftClick()) {
                    OreManager.removeOre(item.getType());
                    open(player, Utils.readInt(item, "current"));
                } else {
                    Ore ore = OreManager.getOre(item.getType());
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        player.sendMessage(mm.deserialize(Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")) + " will not drop anything."));
                        ore.setDrop(null);
                    } else {
                        ore.setDrop(player.getInventory().getItemInMainHand());
                        player.sendMessage(mm.deserialize(Utils.toTitleCase(ore.getType().toString().toLowerCase().replace("_", " ")) + " will now drop " + ore.getDrop().getType().toString().toLowerCase().replace("_", " ")));
                    }
                    OreManager.saveOre(ore);
                    player.closeInventory();
                    return;
                }
            }
        }
        if (Utils.readInt(item, "page") != null) {
            open(player, Utils.readInt(item, "page"));
        }
    }


}
