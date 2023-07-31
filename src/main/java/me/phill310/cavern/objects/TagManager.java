package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

public class TagManager {
    private static final Main plugin = Main.getPlugin(Main.class);
    private static final LinkedHashMap<String, Tag> tags = new LinkedHashMap<>();
    private static final MiniMessage mm = MiniMessage.miniMessage();


    public static void setup() {
        reload();
    }
    public static void reload() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("tags");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        if (config != null) {
            tags.clear();
            for (String key : config.getKeys(false)) {
                ConfigurationSection data = plugin.getConfig().getConfigurationSection("tags." + key);
                Tag tag = new Tag(key, mm.deserialize(data.getString("description")), mm.deserialize(data.getString("display")), data.getLong("released"), df.format(new Date(data.getLong("released"))));
                tags.put(key, tag);
            }
        }
    }

    public static void save() {

    }

    public static Set<String> getTags() {
        return tags.keySet();
    }

    public static Tag getTag(String name, long unlocked) {
        Tag tag = tags.get(name);
        tag.setUnlocked(unlocked);
        return tag;
    }

    public static Tag getTag(String name) {
        return tags.get(name);
    }
    public static boolean isTag(String name) {
        return tags.containsKey(name);
    }

    public static void createTag(Tag tag) {
        tags.put(tag.getName(), tag);
    }

    public static void giveTag(Player player, String name) {
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (!profile.hasTag(name)) {
            Tag tag = getTag(name, System.currentTimeMillis());
            profile.addTag(tag);
            ProfileManager.saveProfile(profile);
            player.sendMessage(mm.deserialize("<gray>[<yellow>Tags<gray>] <white>You have unlocked a new tag: ").append(tag.getDisplay()));
        }
    }

    public static void takeTag(Player player, String name) {
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasTag(name)) {
            Tag tag = profile.removeTag(name);
            if (profile.hasSelectedTag()) if (profile.getSelectedTag().equals(tag)) profile.setSelectedTag(null);
            player.sendMessage(mm.deserialize("<gray>[<yellow>Tags<gray>] <white>You have lost a tag: ").append(tag.getDisplay()));
        }
    }


}
