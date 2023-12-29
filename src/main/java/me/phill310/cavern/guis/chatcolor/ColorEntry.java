package me.phill310.cavern.guis.chatcolor;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ColorEntry extends Entry {
    private final String colorCode;
    private final ColorGUI gui;
    public ColorEntry(Material material, String name, int slot, String colorCode, ColorGUI gui) {
        super(material, name, List.of("<red>Locked"), 1, slot);
        this.colorCode = colorCode;
        this.gui = gui;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasColor(colorCode)) {
            if (!profile.getChatcolor().equals(colorCode)) {
                profile.setChatcolor(colorCode);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Chat<gray>] <" + profile.getChatcolor() + ">This is now your chatcolor"));
            }
            ProfileManager.saveProfile(profile);
            gui.update(event.getClickedInventory(), profile);
        }

    }

    public void update(Profile profile) {
        if (!profile.hasColor(colorCode)) {
            super.setLore(List.of("<red>Locked"));
        } else if (profile.getChatcolor().equals(colorCode)) {
            super.setGlowing(true);
            super.setLore(List.of("<green>Active"));
        } else {
            if (super.isGlowing()) super.setGlowing(false);
            super.setLore(List.of("<gray>Click to set your chatcolor to " + getName()));
        }
    }
}
