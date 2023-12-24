package me.phill310.cavern;

import me.phill310.cavern.objects.OreManager;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.TagManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCavern implements TabExecutor {
    private final Main plugin = Main.getPlugin(Main.class);
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("save")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("ore")) {
                        OreManager.saveAll();
                        sender.sendMessage(Component.text("Ores have been saved!"));
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("profile")) {
                        ProfileManager.saveAll();
                        sender.sendMessage(Component.text("Profiles have been saved!"));
                    }
                } else {
                    ProfileManager.saveAll();
                    OreManager.saveAll();
                    sender.sendMessage(Component.text("Everything has been saved!"));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("ore")) {
                        OreManager.setup();
                        sender.sendMessage(Component.text("Ores have been reloaded!"));
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("profile")) {
                        ProfileManager.setup();
                        sender.sendMessage(Component.text("Profiles have been reloaded!"));
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("config")) {
                        plugin.reloadConfig();
                        sender.sendMessage(Component.text("Config has been reloaded"));
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("tags")) {
                        TagManager.reload();
                    }
                } else {
                    plugin.reloadConfig();
                    ProfileManager.setup();
                    OreManager.setup();
                    TagManager.reload();
                    sender.sendMessage(Component.text("Everything has been reloaded!"));
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            Collections.addAll(commands, "reload", "save");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                commands.add("config");
            }
            Collections.addAll(commands, "ore", "profile", "all");
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
