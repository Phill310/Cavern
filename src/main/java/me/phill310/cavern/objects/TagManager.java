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

/**
 * Manages tags, moving them between CACHE and config files. Also used to distribute tags
 */
public class TagManager {
    private static final Main plugin = Main.getPlugin(Main.class);
    private static final LinkedHashMap<String, Tag> tags = new LinkedHashMap<>();
    private static final MiniMessage mm = MiniMessage.miniMessage();


    public static void setup() {
        reload();
    }

    /**
     * Load all tags from configuration file into CACHE
     */
    public static void reload() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("tags");

        if (config != null) {
            tags.clear();
            for (String key : config.getKeys(false)) {
                ConfigurationSection data = plugin.getConfig().getConfigurationSection("tags." + key);
                Tag tag = new Tag(key, mm.deserialize(data.getString("description")), mm.deserialize(data.getString("display")), data.getLong("released"));
                tags.put(key, tag);
            }
        }
    }

    public static void save() {

    }

    /**
     * Get a set of all tags in CACHE
     * @return set of tags
     */
    public static Set<String> getTags() {
        return tags.keySet();
    }

    /**
     * Retrieve a tag from CACHE and set its time of unlock
     * @param name name of tag to retrieve
     * @param unlocked unix time of unlock (milliseconds)
     * @return {@link Tag}
     */
    public static Tag getTag(String name, long unlocked) {
        Tag tag = tags.get(name);
        tag.setUnlocked(unlocked);
        return tag;
    }
    /**
     * Retrieve a tag from CACHE
     * @param name name of tag to retrieve
     * @return {@link Tag}
     */
    public static Tag getTag(String name) {
        return tags.get(name);
    }

    /**
     * Check if a tag with the queried name exists
     * @param name tag name to lookup
     * @return true if tag exists
     */
    public static boolean isTag(String name) {
        return tags.containsKey(name);
    }

    /**
     * Register a new tag (store it in CACHE)
     * @param tag tag to be added
     */
    public static void createTag(Tag tag) {
        tags.put(tag.getName(), tag);
    }

    /**
     * Give a tag to a player (adds it to their profile & sends message)
     * @param player player receiving tag
     * @param name name of tag to give
     */
    public static void giveTag(Player player, String name) {
        if (!isTag(name)) return;
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (!profile.hasTag(name)) {
            Tag tag = getTag(name, System.currentTimeMillis());
            profile.addTag(tag);
            ProfileManager.saveProfile(profile);
            player.sendMessage(mm.deserialize("<gray>[<yellow>Tags<gray>] <white>You have unlocked a new tag: ").append(tag.getDisplay()));
        }
    }

    /**
     * Revoke a take from a player (removes from profile & sends message)
     * @param player player losing tag
     * @param name name of tag being taken
     */
    public static void takeTag(Player player, String name) {
        Profile profile = ProfileManager.loadProfile(player.getUniqueId());
        if (profile.hasTag(name)) {
            Tag tag = profile.removeTag(name);
            if (profile.hasSelectedTag()) if (profile.getSelectedTag().equals(tag)) profile.setSelectedTag(null);
            ProfileManager.saveProfile(profile);
            player.sendMessage(mm.deserialize("<gray>[<yellow>Tags<gray>] <white>You have lost a tag: ").append(tag.getDisplay()));
        }
    }


}
