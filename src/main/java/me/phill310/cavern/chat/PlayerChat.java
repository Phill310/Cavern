package me.phill310.cavern.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.phill310.cavern.Main;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.Tag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlayerChat implements Listener {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final CustomRenderer cr = new CustomRenderer();
    private final Economy eco = Main.getEconomy();
    Chat chat = Main.getChat();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
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

            if (profile.hasSelectedTag()) {
                Tag tag = profile.getSelectedTag();
                Component hover = tag.getDisplay();
                hover = hover.hoverEvent(
                        HoverEvent.showText(tag.getDisplay()
                                .append(Component.newline())
                                .append(mm.deserialize("<gray>◆ <#32a8a4>Date Released: <white>" + tag.getCreatedFormatted()))
                                .append(Component.newline())
                                .append(mm.deserialize("<gray>◆ <#32a8a4>Date Obtained: <white>" + tag.getUnlockedFormatted()))
                        )
                );
                output = output.append(hover).append(Component.space());
            }

            Component msg = message.hoverEvent(null);
            msg = mm.deserialize("<" + profile.getChatcolor() + ">" + (profile.isBold() ? "<bold>" : "") + "<msg>", Placeholder.component("msg", msg));
            if (source.hasPermission("chat.admin")) {
                msg = mm.deserialize("<reset><" + profile.getChatcolor() + ">" + (profile.isBold() ? "<bold>" : "") + ((TextComponent) message).content());
            }

            output = output.append(mm.deserialize("<gray><b>≫</b> ")).append(msg);

            return output;
        }
    }
}