package me.phill310.cavern.guis.tag;

import me.phill310.cavern.guis.master.Entry;
import me.phill310.cavern.objects.Profile;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.Tag;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class TagEntry extends Entry {
    public TagEntry(Tag tag, TagGUI gui) {
        super(Material.GRAY_DYE, "Locked", null, 1, 0);
        this.tag = tag;
        this.gui = gui;
    }

    private Tag tag;
    private final TagGUI gui;

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasTag(tag.getName())) {
            if (profile.hasSelectedTag() && profile.getSelectedTag().equals(tag)) {
                profile.setSelectedTag(null);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Tags<gray>] <white>You have unequipped your tag!"));
            } else {
                profile.setSelectedTag(tag);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Tags<gray>] <white>You have equipped tag: ").append(tag.getDisplay()));
            }
            ProfileManager.saveProfile(profile);
            gui.update(event.getClickedInventory(), profile);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<yellow>Tags<gray>] <red>You have not unlocked this tag!"));
        }
    }

    public void setup(Profile profile) {
        if (profile.hasTag(tag.getName())) {
            super.setMaterial(Material.NAME_TAG);
            this.tag = profile.getTag(tag.getName());
        }
        super.setName(MiniMessage.miniMessage().serialize(tag.getDisplay()));
        update(profile);
    }

    public void update(Profile profile) {
        List<String> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage().serialize(tag.getDescription()));
        lore.add("<gray>◆ <#32a8a4>Date Released: <white>" + tag.getCreatedFormatted());
        if (profile.hasTag(tag.getName())) {
            lore.add("<gray>◆ <#32a8a4>Date Obtained: <white>" + tag.getUnlockedFormatted());
            if (profile.hasSelectedTag() && tag.equals(profile.getSelectedTag())) {
                lore.add("<red>");
                lore.add("<red>Click to unequip!");
                super.setGlowing(true);
            } else {
                lore.add("<green>");
                lore.add("<green>Click to equip!");
                super.setGlowing(false);
            }
        } else {
            lore.add("<red>");
            lore.add("<red>You have not unlocked this tag!");
        }
        super.setLore(lore);
    }
}
