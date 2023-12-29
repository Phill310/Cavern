package me.phill310.cavern.chat.commands;

import me.phill310.cavern.Main;
import me.phill310.cavern.guis.tag.TagGUI;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.TagManager;
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

import java.util.ArrayList;
import java.util.Collections;
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
                    TagGUI gui = new TagGUI("tag", MiniMessage.miniMessage().deserialize("<blue><bold>Tags"), Main.getGuiManager());
                    Main.getGuiManager().openGUI(player, gui);
                }
            } else {
                TagGUI gui = new TagGUI("tag", MiniMessage.miniMessage().deserialize("<blue><bold>Tags"), Main.getGuiManager());
                Main.getGuiManager().openGUI(player, gui);
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

}
