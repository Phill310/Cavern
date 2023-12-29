package me.phill310.cavern.guis.chatcolor;

import me.phill310.cavern.Main;
import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class RandomEntry extends Entry {
    private final String colorCode;
    private final ColorGUI gui;
    public RandomEntry(Material material, String name, int slot, ColorGUI gui) {
        super(material, name, List.of("<red>Locked"), 1, slot);
        this.gui = gui;
        this.colorCode = generateColor();
        super.setName("<" + colorCode + ">Random Color");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasColor("random")) {
            if (!profile.getChatcolor().startsWith("c:")) {
                profile.setChatcolor(colorCode);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Chat<gray>] <" + profile.getChatcolor() + ">This is now your chatcolor"));
            }
            ProfileManager.saveProfile(profile);
            gui.update(event.getClickedInventory(), profile);
        }
    }

    public void update(Profile profile) {
        if (!profile.hasColor("random")) {
            super.setLore(List.of("<red>Locked"));
        } else if (profile.getChatcolor().equals(colorCode)) {
            super.setGlowing(true);
            super.setLore(List.of("<green>Active"));
        } else {
            if (super.isGlowing()) super.setGlowing(false);
            super.setLore(List.of("<gray>Click to set your chatcolor to <" + colorCode + ">" + colorCode.replace("c:", "")));
        }
    }

    public String generateColor() {
        return "c:" + String.format("#%06x", Main.random.nextInt(0xffffff + 1));
    }
}
