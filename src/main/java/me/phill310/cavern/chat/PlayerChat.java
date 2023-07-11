package me.phill310.cavern.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.phill310.cavern.Main;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.Tag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerChat implements Listener {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final CustomRenderer cr = new CustomRenderer();
    private final Economy eco = Main.getEconomy();
    Chat chat = Main.getChat();
    private static ArrayList<Player> muted = new ArrayList<>();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if (muted.contains(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        event.renderer(cr);
    }

    private class CustomRenderer implements ChatRenderer {
        @Override
        public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
            Profile profile = ProfileManager.loadProfile(source.getUniqueId());
            Component output = Component.empty();

            if (chat.getPlayerPrefix(source) != null) {
                output = mm.deserialize("<reset>").append(mm.deserialize(chat.getPlayerPrefix(source))).append(Component.space());
            }

            output = output.append(mm.deserialize("<reset>").append(sourceDisplayName).append(Component.space())).hoverEvent(HoverEvent.showText(mm.deserialize(chat.getPlayerPrefix(source) + "<reset> " + source.getName() + "\n<white>Balance: <green>$" + eco.getBalance(source) + "\n\n<white><u>Click to Message " + source.getName()))).clickEvent(ClickEvent.suggestCommand("/w " + source.getName() + " "));

            if (profile.hasTag()) {
                //Bukkit.broadcast(Component.text("Has a tag"));
                Tag tag = profile.getTag();
                Component hover = tag.getDisplay();
                hover = hover.hoverEvent(HoverEvent.showText(mm.deserialize("Date Obtained: " + tag.getUnlocked())));
                output = output.append(hover).append(Component.space());
            }

            Component msg = message;
            msg = msg.color(TextColor.fromHexString(profile.getChatcolor()));
            if (source.hasPermission("chat.admin")) {
                msg = mm.deserialize("<reset><white>" + ((TextComponent) message).content());
            }

            output = output.append(mm.deserialize("<gray><b>≫</b> ")).append(msg);

            return output;


            /*return mm.deserialize(
                    "<tag> <prefix> <display_name> <gray><b>≫</b> <message>",
                    Placeholder.parsed("player", "<reset>" + source.getName()),
                    Placeholder.component("tag", mm.deserialize("<reset><red>Test Tag").hoverEvent(HoverEvent.showText(mm.deserialize("<red>Test Tag\nObtained on 6/13")))),
                    Placeholder.component("display_name", mm.deserialize("<reset>").append(sourceDisplayName).hoverEvent(HoverEvent.showText(mm.deserialize(chat.getPlayerPrefix(source) + " " + source.getName() + "\n<green>$4.20m\n\n<o>Click to Message " + source.getName()))).clickEvent(ClickEvent.suggestCommand("/w " + source.getName() + " "))),
                    Placeholder.parsed("prefix", "<reset>" + chat.getPlayerPrefix(source)),
                    Placeholder.parsed("suffix", "<reset>" + chat.getPlayerSuffix(source)),
                    Placeholder.component("message", msg)
            );*/
        }
    }

    public static void mute(Player player) {
        muted.add(player);
    }

    public static void unmute(Player player) {
        muted.remove(player);
    }
}