package me.phill310.cavern.chat.commands;

import me.phill310.cavern.Utils;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.Tag;
import me.phill310.cavern.objects.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandTag implements TabExecutor, Listener {

    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("give") && player.hasPermission("tags.give")) {
                    Player target;
                    String tag;
                    if (args.length >= 3) {
                        target = Bukkit.getPlayer(args[1]);
                        tag = args[2];
                        if (target == null) {
                            player.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                            return true;
                        }
                    } else if (args.length == 2) {
                        target = player;
                        tag = args[1];
                    } else {
                        return false;
                    }
                    if (TagManager.isTag(tag)) {
                        Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                        if (!profile.hasTag(tag)) {
                            TagManager.giveTag(target, tag);
                            player.sendMessage(mm.deserialize("<white>Gave <aqua>" + target.getName() + "<white> the tag <yellow>" + tag));
                        } else {
                            player.sendMessage(mm.deserialize("<aqua>" + target.getName() + " <red>already has this tag!"));
                        }
                    } else {
                        player.sendMessage(mm.deserialize("<red>That is not a tag!"));
                    }

                } else if (args[0].equalsIgnoreCase("remove") && player.hasPermission("tags.remove")) {
                    Player target;
                    String tag;
                    if (args.length >= 3) {
                        target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                            return true;
                        }
                        tag = args[2];
                    } else if (args.length == 2) {
                        target = player;
                        tag = args[1];
                    } else {
                        return false;
                    }
                    if (TagManager.isTag(tag)) {
                        Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                        if (profile.hasTag(tag)) {
                            TagManager.takeTag(target, tag);
                            player.sendMessage(mm.deserialize("<aqua>" + target.getName() + "<white> no longer has the tag " + tag));
                        } else {
                            player.sendMessage(mm.deserialize("<aqua>" + target.getName() + " <red>already doesn't have this tag!"));
                        }
                    } else {
                        player.sendMessage(mm.deserialize("<red>That is not a tag!"));
                    }
                } else {
                    open(player);
                }
            } else {
                open(player);
            }
        } else {
            if (args.length >= 3) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    commandSender.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                    return true;
                }
                if (!TagManager.isTag(args[2])) {
                    commandSender.sendMessage(Component.text(args[2] + " is not a valid tag!"));
                    return true;
                }
                Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                if (args[0].equalsIgnoreCase("give")) {
                    if (profile.hasTag(args[2])) {
                        commandSender.sendMessage(Component.text(target.getName() + "already has this tag!"));
                    } else {
                        TagManager.giveTag(target, args[2]);
                        commandSender.sendMessage(Component.text("Gave " + target.getName() + " tag " + args[2]));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!profile.hasTag(args[2])) {
                        commandSender.sendMessage(Component.text(target.getName() + " doesn't have this tag!"));
                    } else {
                        TagManager.takeTag(target, args[2]);
                        commandSender.sendMessage(Component.text("Removed " + target.getName() + "'s tag " + args[2]));
                    }
                }
            } else {
                commandSender.sendMessage(Component.text("/tag <give/remove> <tag>"));
                return true;
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (!(commandSender.hasPermission("tags.give") || commandSender.hasPermission("tags.remove"))) {
            return completions;
        }

        if (args.length == 1) {
            Collections.addAll(commands, "give", "remove");
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                commands.add(player.getName());
            }
        } else if (args.length == 3) {
            Profile profile = ProfileManager.loadProfile(Bukkit.getPlayerUniqueId(args[1]));
            for (String tag : TagManager.getTags()) {
                if (profile.hasTag(tag)) {
                    if (args[0].equalsIgnoreCase("remove")) commands.add(tag);
                } else {
                    if (args[0].equalsIgnoreCase("give")) commands.add(tag);
                }

            }
        }

        StringUtil.copyPartialMatches(args[args.length-1], commands, completions);
        Collections.sort(completions);
        return completions;
    }


    private void open(Player player) {
        open(player, 0);
    }
    private void open(Player player, int page) {
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        int size = (int) ((Math.ceil(((double) TagManager.getTags().size())/9d)+1))*9;
        boolean next = false;
        if (size >= 54) {
            size = 54;
            next = true;
        }
        Component name = mm.deserialize("<blue><b>Tags (Page " + page + ")");
        if (page == 0) name = mm.deserialize("<blue><b>Tags");
        Inventory inv = Bukkit.createInventory(null, size, name);
        LinkedList<ItemStack> locked = new LinkedList<>();
        for (String tagName : TagManager.getTags()) {
            ItemStack item;
            Tag tag;
            if (profile.hasTag(tagName)) {
                tag = profile.getTag(tagName);
                item = new ItemStack(Material.NAME_TAG);
            } else {
                tag = TagManager.getTag(tagName);
                item = new ItemStack(Material.GRAY_DYE);
            }

            ItemMeta meta = item.getItemMeta();
            meta.displayName(mm.deserialize("<!i>").append(tag.getDisplay()));
            List<Component> lore = new ArrayList<>();
            lore.add(mm.deserialize("<!i><gray>").append(tag.getDescription()));
            lore.add(mm.deserialize("<!i><gray>◆ <#32a8a4>Date Released: <white>" + tag.getCreatedFormatted()));
            if (profile.hasTag(tagName)) {
                lore.add(mm.deserialize("<!i><gray>◆ <#32a8a4>Date Obtained: <white>" + tag.getUnlockedFormatted()));
                if (profile.hasSelectedTag() && tag.equals(profile.getSelectedTag())) {
                    lore.add(mm.deserialize("<!i><red>Click to unequip!"));
                    meta.addEnchant(Enchantment.LUCK, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    lore.add(mm.deserialize("<!i><green>Click to equip!"));
                }
            } else {
                lore.add(Component.space());
                lore.add(mm.deserialize("<!i><red>You have not unlocked this tag!"));
            }

            meta.lore(lore);
            item.setItemMeta(meta);
            if (profile.hasTag(tagName)) {
                inv.addItem(Utils.addInt(Utils.addString(item, "tag", tag.getName()), "page", page));
            } else {
                locked.add(Utils.addInt(Utils.addString(item, "tag", tag.getName()), "page", page));
            }
        }
        for (ItemStack item : locked) {
            inv.addItem(item);
        }
        ItemStack glass = Utils.addString(Utils.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, ""), "inv", "tags");
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
        if ((Utils.readString(testItem, "inv") == null) || (!Utils.readString(testItem, "inv").equals("tags"))) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (Utils.readString(item, "tag") != null) {
            Profile profile = ProfileManager.loadProfile(player.getUniqueId());
            Tag clicked = profile.getTag(Utils.readString(item, "tag"));
            if (profile.hasSelectedTag() && clicked.equals(profile.getSelectedTag())) {
                profile.setSelectedTag(null);
            } else {
                profile.setSelectedTag(clicked);
            }
            ProfileManager.saveProfile(profile);
        }
        if (Utils.readInt(item, "page") != null) {
            open(player, Utils.readInt(item, "page"));
        }
    }

}
