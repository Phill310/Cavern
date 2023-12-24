package me.phill310.cavern.objects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Holds important information about a player<br><br>
 * Ingame Data: <br>
 * - {@link Profile#getUuid()}<br>
 * - {@link Profile#getTags()}<br>
 * - {@link Profile#getColors()}
 */
public class Profile {

    //TODO: Store tags within the profile to allow for date obtained and # of owners
    private final UUID uuid;
    //private Component nick = null;
    private final HashMap<String, Tag> tags = new HashMap<>();
    private Tag tag = null;
    private String chatcolor = "gray";
    private List<String> colors = new ArrayList<>();
    private boolean bold;

    /**
     * Create a new profile
     * @param uuid uuid of the profile's owner
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        colors.add("gray");
    }

    /**
     * Load a profile from an existing configuration file
     * @param data configuration file with playerdata
     */
    public Profile(FileConfiguration data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        if (data.getConfigurationSection("tags") != null) {
            for (String key: data.getConfigurationSection("tags").getKeys(false)) {
                tags.put(key, TagManager.getTag(key, data.getLong("tags." + key)));
            }
            tag = (data.getString("tag") != null) ? tags.get(data.getString("tag")) : null;
        }
        colors = data.getStringList("colors");
        if (!colors.contains("gray")) colors.add("gray");
        chatcolor = data.getString("chatcolor", "gray");
        bold = data.getBoolean("bold", false);
        //nick = (data.getString("nick") == null) ? mm.deserialize(data.getString("nick")) : null;

    }

    /**
     * Get the uuid of this profile
     * @return uuid of the owner of the profile
     */
    public UUID getUuid() { return uuid; }

    /*public boolean hasNick() { return nick != tag; }
    public Component getNick() { return nick; }
    public void setNick(Component nick) { this.nick = nick; }*/

    /**
     * Check if this profile has a tag selected (active)
     * @return true if a tag is selected
     */
    public boolean hasSelectedTag() { return tag != null; }

    /**
     * Get the tag that this profile has selected
     * @return selected {@link Tag}
     */
    public Tag getSelectedTag() { return tag; }

    /**
     * Select a tag for this profile
     * @param tag tag to be selected
     */
    public void setSelectedTag(Tag tag) { this.tag = tag; }

    /**
     * Get a list of all tags this profile has unlocked
     * @return map of tags
     */
    public HashMap<String, Tag> getTags() { return tags; }

    /**
     * Give this profile a new tag
     * @param tag tag player is unlocking
     */
    public void addTag(Tag tag) { tags.put(tag.getName(), tag); }

    /**
     * Remove a tag from this profile
     * @param tag tag to be revoked
     */
    public void removeTag(Tag tag) {
        tags.remove(tag.getName());
    }

    /**
     * Remove a tag from this profile
     * @param tag tag to be revoked
     * @return {@link Tag} that got removed
     */
    public Tag removeTag(String tag) {
        return tags.remove(tag);
    }

    /**
     * Check if the profile has the queried tag
     * @param name name of the tag being checked
     * @return true if profile has tag
     */
    public boolean hasTag(String name) {
        return tags.containsKey(name);
    }

    /**
     * Get a {@link Tag} from its name<br>
     * includes data about when it was obtained
     * @param name name of the tag
     * @return {@link Tag} class for queried tag
     */
    public Tag getTag(String name) {
        return tags.get(name);
    }

    /**
     * Get the profile's selected chatcolor<br>
     * Color must be put in <> before it can be used in minimessage
     * @return color string
     */
    public String getChatcolor() { return chatcolor; }

    /**
     * Set the profile's active chatcolor<br>
     * This should not include any <>
     * @param chatcolor the new color
     */
    public void setChatcolor(String chatcolor) { this.chatcolor = chatcolor; }

    /**
     * Get a list of all colors the profile has unlocked
     * @return list of color code strings
     */
    public List<String> getColors() { return colors; }

    /**
     * Add a color to the profile's unlocked colors
     * @param color color string to be added
     */
    public void addColor(String color) { colors.add(color); }

    /**
     * Remove a color from the profile's unlocked colors
     * @param color color string to be removed
     */
    public void removeColor(String color) {
        colors.remove(color);
        if (chatcolor.equals(color)) chatcolor = "gray";
        if (chatcolor.equals("bold")) if (isBold()) toggleBold();
    }

    /**
     * Check if the profile has unlocked a color
     * @param color color string to be checked
     * @return true if the profile has the color
     */
    public boolean hasColor(String color) { return colors.contains(color); }

    /**
     * Check if the profile has bold chat activated
     * @return true if bold chat is activated
     */
    public boolean isBold() { return bold; }

    /**
     * Toggle if bold chat is active<br>
     * True -> False<br>
     * False -> True
     */
    public void toggleBold() { bold = !bold; }
}