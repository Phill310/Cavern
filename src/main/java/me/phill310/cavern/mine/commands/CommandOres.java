package me.phill310.cavern.mine.commands;

import me.phill310.cavern.Main;
import me.phill310.cavern.guis.ores.OreGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandOres implements TabExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            OreGUI gui = new OreGUI("ore", MiniMessage.miniMessage().deserialize("<dark_gray>Ores"), Main.getGuiManager());
            Main.getGuiManager().openGUI(player, gui);
        } else {
            commandSender.sendMessage(Component.text("That aint gonna work chief"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
