package me.phill310.cavern.guis.chatcolor;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.guis.master.GUI;
import me.phill310.cavern.guis.master.GUIManager;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class ColorGUI extends GUI {
    private final GUIManager manager;
    public ColorGUI(String identifier, int size, Component title, GUIManager manager) {
        super(identifier, size, title);
        this.manager = manager;
        addEntry(new ColorEntry(Material.REDSTONE, "<dark_red>Dark Red", 10, "dark_red", this));
        addEntry(new ColorEntry(Material.RED_DYE, "<red>Red", 11, "red", this));
        addEntry(new ColorEntry(Material.ORANGE_DYE, "<gold>Orange", 12, "gold", this));
        addEntry(new ColorEntry(Material.YELLOW_DYE, "<yellow>Yellow", 13, "yellow", this));
        addEntry(new ColorEntry(Material.LIME_DYE, "<green>Lime", 14, "green", this));
        addEntry(new ColorEntry(Material.GREEN_DYE, "<dark_green>Green", 15, "dark_green", this));
        addEntry(new ColorEntry(Material.LIGHT_BLUE_DYE, "<aqua>Light Blue", 16, "aqua", this));
        addEntry(new ColorEntry(Material.CYAN_DYE, "<dark_aqua>Cyan", 20, "dark_aqua", this));
        addEntry(new ColorEntry(Material.BLUE_DYE, "<blue>Blue", 21, "blue", this));
        addEntry(new ColorEntry(Material.PINK_DYE, "<light_purple>Pink", 22, "light_purple", this));
        addEntry(new ColorEntry(Material.PURPLE_DYE, "<dark_purple>Purple", 23, "dark_purple", this));
        addEntry(new ColorEntry(Material.BROWN_DYE, "<#964B00>Brown", 24, "#964B00", this));
        addEntry(new ColorEntry(Material.WHITE_DYE, "<white>White", 30, "white", this));
        addEntry(new ColorEntry(Material.LIGHT_GRAY_DYE, "<gray>Light Gray", 31, "gray", this));
        addEntry(new ColorEntry(Material.GRAY_DYE, "<dark_gray>Gray", 32, "dark_gray", this));
        addEntry(new RandomEntry(Material.SUSPICIOUS_STEW, "<random>Random Color", 37, this));
        addEntry(new ColorEntry(Material.DRAGON_BREATH, "<rainbow>Rainbow", 40, "rainbow", this));
        addEntry(new BoldEntry(Material.BEACON, "<white><bold>Bold", 43, this));
        fill();
    }

    public void update(Inventory inventory, Profile profile) {
        update(profile);
        manager.updateGUI(inventory, this);
    }

    public void update(Profile profile) {
        for (Entry entry : super.getEntries().values()) {
            if (entry instanceof ColorEntry) {
                ((ColorEntry) entry).update(profile);
            } else if (entry instanceof RandomEntry) {
                ((RandomEntry) entry).update(profile);
            } else if (entry instanceof BoldEntry) {
                ((BoldEntry) entry).update(profile);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        update(event.getInventory(), ProfileManager.loadProfile(event.getPlayer().getUniqueId()));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
