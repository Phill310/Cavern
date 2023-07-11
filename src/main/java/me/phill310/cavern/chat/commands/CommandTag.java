package me.phill310.cavern.chat.commands;

import me.phill310.cavern.Utils;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.Profile;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandTag implements TabExecutor {

    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());

        if (args.length >= 1) {
            //TODO: Make a loader to convert tag strings into tags
            //profile.setTag(mm.deserialize(String.join(" ", args)));
            //player.sendMessage(mm.deserialize("Your tag is now <newline>").append(profile.getTag()));
        } else {
            profile.setTag(null);
            player.sendMessage(mm.deserialize("You no longer have a tag"));
        }
        ProfileManager.saveProfile(profile);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
