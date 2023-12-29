package me.phill310.cavern.chat.commands;

import me.phill310.cavern.Main;
import me.phill310.cavern.guis.chatcolor.ColorGUI;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandChatcolor implements Listener, TabExecutor {
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final HashMap<String, String> names = new HashMap<>();

    public CommandChatcolor() {
        names.put("dark red", "dark_red");
        names.put("red", "red");
        names.put("orange", "gold");
        names.put("yellow", "yellow");
        names.put("lime", "green");
        names.put("green", "dark_green");
        names.put("light blue", "aqua");
        names.put("cyan", "dark_aqua");
        names.put("blue", "blue");
        names.put("pink", "light_purple");
        names.put("purple", "dark_purple");
        names.put("brown", "#964B00");
        names.put("white", "white");
        names.put("light gray", "gray");
        names.put("gray", "dark_gray");
        names.put("random", "random");
        names.put("rainbow", "rainbow");
        names.put("bold", "bold");
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
                    //open(player);
                    ColorGUI gui = new ColorGUI("color", 54, MiniMessage.miniMessage().deserialize("<dark_gray>Chatcolors"), Main.getGuiManager());
                    Main.getGuiManager().openGUI(player, gui);
                }
            } else {
                //open(player);
                ColorGUI gui = new ColorGUI("color", 54, MiniMessage.miniMessage().deserialize("<dark_gray>Chatcolors"), Main.getGuiManager());
                Main.getGuiManager().openGUI(player, gui);
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
                    sender.sendMessage(Component.text(colorName + " is not a valid chatcolor!"));
                    return true;
                }
                Profile profile = ProfileManager.loadProfile(target.getUniqueId());
                String color = names.get(colorName);
                if (args[0].equalsIgnoreCase("give")) {
                    if (profile.hasColor(color)) {
                        sender.sendMessage(Component.text(target.getName() + "already has this chatcolor!"));
                    } else {
                        profile.addColor(color);
                        ProfileManager.saveProfile(profile);
                        sender.sendMessage(Component.text("Gave " + target.getName() + " the chatcolor " + colorName));
                        target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have unlocked a new chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!profile.hasColor(color)) {
                        sender.sendMessage(Component.text(target.getName() + " doesn't have this chatcolor!"));
                    } else {
                        profile.removeColor(color);
                        ProfileManager.saveProfile(profile);
                        sender.sendMessage(Component.text("Removed " + target.getName() + "'s chatcolor " + colorName));
                        target.sendMessage(mm.deserialize(("<gray>[<yellow>Chat<gray>] <white>You have lost the chatcolor: <" + color + ">" + colorName).replace("<random>", "")));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    profile.setChatcolor(colorName);
                    ProfileManager.saveProfile(profile);
                    sender.sendMessage(Component.text(target.getName() + "'s chatcolor is now " + colorName));
                    target.sendMessage(mm.deserialize("<gray>[<yellow>Chat<gray>] <" + color + ">This is your new chatcolor!"));
                }
            } else {
                sender.sendMessage(Component.text("/chatcolor <give/remove/set> <player> <color>"));
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
            for (String key : names.keySet()) {
                if (profile.hasColor(names.get(key))) {
                    if (args[0].equalsIgnoreCase("remove")) commands.add(key);
                } else {
                    if (args[0].equalsIgnoreCase("give")) commands.add(key);
                }
            }
        }

        StringUtil.copyPartialMatches(args[args.length-1], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}
