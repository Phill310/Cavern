package me.phill310.cavern.guis.chatcolor;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class BoldEntry extends Entry {
    private final ColorGUI gui;
    public BoldEntry(Material material, String name, int slot, ColorGUI gui) {
        super(material, name, List.of("<red>Locked"), 1, slot);
        this.gui = gui;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasColor("bold")) {
            profile.toggleBold();
            if (profile.isBold()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Chat<gray>] <white>You chat will now be bold"));
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Chat<gray>] <white>You chat will no longer be bold"));
            }
            ProfileManager.saveProfile(profile);
            gui.update(event.getClickedInventory(), profile);
        }
    }

    public void update(Profile profile) {
        if (!profile.hasColor("bold")) {
            setLore(List.of("<red>Locked"));
        } else if (profile.isBold()) {
            if (!super.isGlowing()) super.setGlowing(true);
            setLore(List.of("<gray>Make your chat <bold>bold", "<green>Click to turn off"));
        } else {
            if (super.isGlowing()) super.setGlowing(false);
            setLore(List.of("<gray>Make your chat <bold>bold", "<red>Click to turn on"));
        }
    }
}
