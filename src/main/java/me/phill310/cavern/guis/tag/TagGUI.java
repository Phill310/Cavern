package me.phill310.cavern.guis.tag;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.guis.master.GUIManager;
import me.phill310.cavern.guis.master.PaginatedGUI;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.TagManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class TagGUI extends PaginatedGUI {
    public TagGUI(String identifier, Component title, GUIManager manager) {
        super(identifier, title, manager);
        for (String tagName : TagManager.getTags()) {
            addEntry(new TagEntry(TagManager.getTag(tagName), this));
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        Profile profile = ProfileManager.loadProfile(event.getPlayer().getUniqueId());
        for (Entry entry : super.getAllEntries()) {
            if (entry instanceof TagEntry) {
                ((TagEntry) entry).setup(profile);
            }
        }
        super.generatePage();
        super.loadPage(event.getInventory());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    public void update(Inventory inventory, Profile profile) {
        update(profile);
        super.loadPage(inventory);
    }

    public void update(Profile profile) {
        for (Entry entry : super.getAllEntries()) {
            if (entry instanceof TagEntry) {
                ((TagEntry) entry).update(profile);
            }
        }
        super.generatePage();
    }


}
