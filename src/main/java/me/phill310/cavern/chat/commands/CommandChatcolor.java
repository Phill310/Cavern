package me.phill310.cavern.chat.commands;

import me.phill310.cavern.Main;
import me.phill310.cavern.Utils;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
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

import java.util.*;

public class CommandChatcolor implements Listener, TabExecutor {
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final HashMap<Material, String> colors = new HashMap<>();
    private final HashMap<String, String> names = new HashMap<>();

    public CommandChatcolor() {
        colors.put(Material.REDSTONE, "dark_red");
        colors.put(Material.RED_DYE, "red");
        colors.put(Material.ORANGE_DYE, "gold");
        colors.put(Material.YELLOW_DYE, "yellow");
        colors.put(Material.LIME_DYE, "green");
        colors.put(Material.GREEN_DYE, "dark_green");
        colors.put(Material.LIGHT_BLUE_DYE, "aqua");
        colors.put(Material.CYAN_DYE, "dark_aqua");
        colors.put(Material.BLUE_DYE, "blue");
        colors.put(Material.PINK_DYE, "light_purple");
        colors.put(Material.PURPLE_DYE, "dark_purple");
        colors.put(Material.BROWN_DYE, "#964B00");
        colors.put(Material.WHITE_DYE, "white");
        colors.put(Material.LIGHT_GRAY_DYE, "gray");
        colors.put(Material.GRAY_DYE, "dark_gray");
        colors.put(Material.SUSPICIOUS_STEW, "random");
        colors.put(Material.DRAGON_BREATH, "rainbow");
        colors.put(Material.BEACON, "bold");
        for (Material type : colors.keySet()) {
            names.put(nameFixer(type), colors.get(type));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("give") && player.hasPermission("chatcolor.give")) {
                    Player target;
                    String colorName;
                    if (args.length >= 3) {
                        target = Bukkit.getPlayer(args[1]);
                        if (!sender.hasPermission("chatcolor.others")) target = player;
                        colorName = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
                        if (target == null) {
                            player.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                            return true;
                        }
                    } else {
                        player.sendMessage(mm.deserialize("<red>/chatcolor <give/remove/set> <player> <color>"));
                        return true;
                    }
                    if (names.containsKey(colorName)) {
                        String color = names.get(colorName);
                        Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                        if (!profile.hasColor(color)) {
                            profile.addColor(color);
                            ProfileManager.saveProfile(profile);
                            if (target != player) player.sendMessage(mm.deserialize(("<white>Gave <aqua>" + target.getName() + "<white> a new chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                            target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have unlocked a new chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                        } else {
                            player.sendMessage(mm.deserialize("<aqua>" + target.getName() + " <red>already has this chatcolor!"));
                        }
                    } else {
                        player.sendMessage(mm.deserialize("<red>That is not a chatcolor!"));
                    }

                } else if (args[0].equalsIgnoreCase("remove") && player.hasPermission("chatcolor.remove")) {
                    Player target;
                    String colorName;
                    if (args.length >= 3) {
                        target = Bukkit.getPlayer(args[1]);
                        if (!sender.hasPermission("chatcolor.others")) target = player;
                        if (target == null) {
                            player.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                            return true;
                        }
                        colorName = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
                    } else {
                        player.sendMessage(mm.deserialize("<red>/chatcolor <give/remove/set> <player> <color>"));
                        return true;
                    }
                    if (names.containsKey(colorName)) {
                        String color = names.get(colorName);
                        Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                        if (profile.hasColor(color)) {
                            profile.removeColor(color);
                            ProfileManager.saveProfile(profile);
                            if (target != player) player.sendMessage(mm.deserialize(("<aqua>" + target.getName() + "<white> no longer has the chatcolor <" + color + ">" + colorName).replace("<random>", "")));
                            target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have lost the chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                        } else {
                            player.sendMessage(mm.deserialize("<aqua>" + target.getName() + " <red>already doesn't have this chatcolor!"));
                        }
                    } else {
                        player.sendMessage(mm.deserialize("<red>That is not a chatcolor!"));
                    }
                }  else if (args[0].equalsIgnoreCase("set")) {
                    Player target;
                    String color;
                    if (args.length >= 3) {
                        target = Bukkit.getPlayer(args[1]);
                        if (!sender.hasPermission("chatcolor.others")) target = player;
                        if (target == null) {
                            player.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                            return true;
                        }
                        color = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
                        Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                        profile.setChatcolor(color);
                        ProfileManager.saveProfile(profile);
                        target.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <" + color + ">This is your new chatcolor!"));
                        if (target != player) player.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <" + color + ">This is " + target.getName() + "'s new chatcolor!"));
                    } else {
                        player.sendMessage(mm.deserialize("<red>/chatcolor <give/remove/set> <player> <color>"));
                        return true;
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
                    sender.sendMessage(mm.deserialize("<red>Couldn't find <yellow>" + args[1]));
                    return true;
                }
                String colorName = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
                if (!names.containsKey(colorName)) {
                    sender.sendMessage(colorName + " is not a valid chatcolor!");
                    return true;
                }
                Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                String color = names.get(colorName);
                if (args[0].equalsIgnoreCase("give")) {
                    if (profile.hasColor(color)) {
                        sender.sendMessage(target.getName() + "already has this chatcolor!");
                    } else {
                        profile.addColor(color);
                        ProfileManager.saveProfile(profile);
                        sender.sendMessage("Gave " + target.getName() + " the chatcolor " + colorName);
                        target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have unlocked a new chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!profile.hasColor(color)) {
                        sender.sendMessage(target.getName() + " doesn't have this chatcolor!");
                    } else {
                        profile.removeColor(color);
                        ProfileManager.saveProfile(profile);
                        sender.sendMessage("Removed " + target.getName() + "'s chatcolor " + colorName);
                        target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have lost the chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    profile.setChatcolor(colorName);
                    ProfileManager.saveProfile(profile);
                    sender.sendMessage(target.getName() + "'s chatcolor is now " + colorName);
                    target.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <" + color + ">This is your new chatcolor!"));
                }
            } else {
                sender.sendMessage("/chatcolor <give/remove/set> <player> <color>");
                return true;
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (!(sender.hasPermission("chatcolor.give") || sender.hasPermission("chatcolor.remove") || sender.hasPermission("chatcolor.set"))) {
            return completions;
        }

        if (args.length == 1) {
            Collections.addAll(commands, "give", "remove", "set");
        } else if (args.length == 2) {
            if (sender.hasPermission("chatcolor.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    commands.add(player.getName());
                }
            } else {
                commands.add(sender.getName());
            }
        } else if (args.length == 3) {
            Profile profile = ProfileManager.loadProfile(Bukkit.getPlayerUniqueId(args[1]));
            for (Material item : colors.keySet()) {
                String name = nameFixer(item);
                if (profile.hasColor(colors.get(item))) {
                    if (args[0].equalsIgnoreCase("remove")) commands.add(name);
                } else {
                    if (args[0].equalsIgnoreCase("give")) commands.add(name);
                }
            }
        }

        StringUtil.copyPartialMatches(args[args.length-1], commands, completions);
        Collections.sort(completions);
        return completions;
    }

    private void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54,  mm.deserialize("<dark_gray>Chatcolors"));

        ItemStack glass = Utils.addString(Utils.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, ""), "inv", "chatcolors");
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, glass);
        }

        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        inv.setItem(10, dye(profile, Material.REDSTONE));
        inv.setItem(11, dye(profile, Material.RED_DYE));
        inv.setItem(12, dye(profile, Material.ORANGE_DYE));
        inv.setItem(13, dye(profile, Material.YELLOW_DYE));
        inv.setItem(14, dye(profile, Material.LIME_DYE));
        inv.setItem(15, dye(profile, Material.GREEN_DYE));
        inv.setItem(16, dye(profile, Material.LIGHT_BLUE_DYE));
        inv.setItem(20, dye(profile, Material.CYAN_DYE));
        inv.setItem(21, dye(profile, Material.BLUE_DYE));
        inv.setItem(22, dye(profile, Material.PINK_DYE));
        inv.setItem(23, dye(profile, Material.PURPLE_DYE));
        inv.setItem(24, dye(profile, Material.BROWN_DYE));
        inv.setItem(30, dye(profile, Material.WHITE_DYE));
        inv.setItem(31, dye(profile, Material.LIGHT_GRAY_DYE));
        inv.setItem(32, dye(profile, Material.GRAY_DYE));



        String color = randColor();
        String lore;
        String lore2 = "";
        boolean tag = false;
        if (profile.hasColor("random")) {
            if (profile.getChatcolor().startsWith("c:")) {
                lore2 = "<green>Active";
            }
            lore = "<gray>Click to set your chatcolor to <" + color + ">" + color.replace("c:", "");
            tag = true;
        } else {
            lore = "<red>Locked";
        }
        ItemStack rand = Utils.buildItem(Material.SUSPICIOUS_STEW, 1, "<" + color + ">Random Color", lore, lore2);
        if (tag) Utils.addString(rand, "color", color);
        inv.setItem(37, rand);

        ItemStack rainbow = new ItemStack(Material.DRAGON_BREATH, 1);
        ItemMeta rMeta = rainbow.getItemMeta();
        rMeta.displayName(mm.deserialize("<!i><rainbow>Rainbow Chat"));
        if (profile.hasColor("rainbow")) {

            if (profile.getChatcolor().equalsIgnoreCase("rainbow")) {
                rMeta.lore(Collections.singletonList(mm.deserialize("<!i><green>Active")));
                rMeta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
                rMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                rMeta.lore(Collections.singletonList(mm.deserialize("<!i><gray>Click to set your chatcolor to <rainbow>Rainbow")));
                Utils.addString(rMeta, "color", "rainbow");
            }
        } else {
            rMeta.lore(Collections.singletonList(mm.deserialize("<!i><red>Locked")));
        }
        rainbow.setItemMeta(rMeta);
        inv.setItem(40, rainbow);

        if (profile.hasColor("bold")) {
            if (profile.isBold()) {
                lore = "<red>Click to turn off";
            } else {
                lore = "<green>Click to turn on";
            }
        } else {
            lore = "<red>Locked";
        }
        ItemStack bold = Utils.buildItem(Material.BEACON, 1, "<b><white>Bold", "<gray>Make your chat <b>bold", lore);
        if (profile.hasColor("bold")) {
            Utils.addString(bold, "toggle", "yep");
            if (profile.isBold()) {
                ItemMeta meta = bold.getItemMeta();
                meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                bold.setItemMeta(meta);
            }
        }
        inv.setItem(43, bold);

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        ItemStack testItem = inv.getItem(53);
        if (testItem == null) return;
        if ((Utils.readString(testItem, "inv") == null) || (!Utils.readString(testItem, "inv").equals("chatcolors"))) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (Utils.readString(item, "color") != null) {
            Profile profile = ProfileManager.loadProfile(player.getUniqueId());
            profile.setChatcolor(Utils.readString(item, "color"));
            player.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <" + profile.getChatcolor() + ">This is now your chatcolor"));
            ProfileManager.saveProfile(profile);
            open(player);
        }
        if (Utils.readString(item, "toggle") != null) {
            Profile profile = ProfileManager.loadProfile(player.getUniqueId());
            profile.toggleBold();
            if (profile.isBold()) {
                player.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <white>You chat will now be bold"));
            } else {
                player.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <white>You chat will no longer be bold"));
            }
            ProfileManager.saveProfile(profile);
            open(player);
        }
    }

    private ItemStack dye(Profile profile, Material type) {
        ItemStack item = new ItemStack(type);
        String name = Utils.toTitleCase(nameFixer(type));
        String color = colors.get(type);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize("<!i><" + color + ">" + name));
        if (profile.hasColor(color)) {
            if (profile.getChatcolor().equalsIgnoreCase(color)) {
                meta.lore(Collections.singletonList(mm.deserialize("<!i><green>Active")));
                meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.lore(Collections.singletonList(mm.deserialize("<!i><gray>Click to set your chatcolor to <" + color + ">" + name)));
                Utils.addString(meta, "color", color);
            }
        } else {
            meta.lore(Collections.singletonList(mm.deserialize("<!i><red>Locked")));
        }
        item.setItemMeta(meta);
        return item;
    }

    private String randColor() {
        return "c:" + String.format("#%06x", Main.random.nextInt(0xffffff + 1));
    }

    private String nameFixer(Material type) {
        return type.toString()
                .toLowerCase()
                .replace("_dye", "")
                .replace("redstone", "dark red")
                .replace("suspicious_stew", "random")
                .replace("dragon_breath", "rainbow")
                .replace("beacon", "bold")
                .replace("_", " ");
    }
}
