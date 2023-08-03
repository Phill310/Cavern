package me.phill310.cavern.objects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Profile {

    //TODO: Store tags within the profile to allow for date obtained and # of owners
    private final UUID uuid;
    //private Component nick = null;
    private final HashMap<String, Tag> tags = new HashMap<>();
    private Tag tag = null;
    private String chatcolor = "gray";
    private List<String> colors = new ArrayList<>();
    private boolean bold;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        colors.add("gray");
    }

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

    public UUID getUuid() { return uuid; }

    /*public boolean hasNick() { return nick != tag; }
    public Component getNick() { return nick; }
    public void setNick(Component nick) { this.nick = nick; }*/

    public boolean hasSelectedTag() { return tag != null; }
    public Tag getSelectedTag() { return tag; }
    public void setSelectedTag(Tag tag) { this.tag = tag; }

    public HashMap<String, Tag> getTags() { return tags; }
    public void addTag(Tag tag) { tags.put(tag.getName(), tag); }
    public void removeTag(Tag tag) { tags.remove(tag.getName()); }
    public Tag removeTag(String tag) { return tags.remove(tag); }
    public boolean hasTag(String name) {
        return tags.containsKey(name);
    }
    public Tag getTag(String name) {
        return tags.get(name);
    }

    public String getChatcolor() { return chatcolor; }
    public void setChatcolor(String chatcolor) { this.chatcolor = chatcolor; }

    public List<String> getColors() { return colors; }
    public void addColor(String color) { colors.add(color); }
    public void removeColor(String color) {
        colors.remove(color);
        if (chatcolor.equals(color)) chatcolor = "gray";
        if (chatcolor.equals("bold")) if (isBold()) toggleBold();
    }
    public boolean hasColor(String color) { return colors.contains(color); }

    public boolean isBold() { return bold; }

    public void toggleBold() { bold = !bold; }
}