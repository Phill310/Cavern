package me.phill310.cavern.objects;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class Profile {

    //TODO: Store tags within the profile to allow for date obtained and # of owners
    private final UUID uuid;
    //private Component nick = null;
    private final HashMap<String, Tag> tags = new HashMap<>();
    private Tag tag = null;
    private String chatcolor = "#AAAAAA";

    private final MiniMessage mm = MiniMessage.miniMessage();

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public Profile(FileConfiguration data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        if (data.getConfigurationSection("tags") != null) {
            for (String key: data.getConfigurationSection("tags").getKeys(false)) {
                tags.put(key, TagManager.getTag(key, data.getLong("tags." + key)));
            }
            tag = (data.getString("tag") != null) ? tags.get(data.getString("tag")) : null;
        }
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
}