package me.phill310.cavern.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
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
        for (String key: data.getConfigurationSection("tags").getKeys(false)) {
            tags.put(key, new Tag(key, data.getLong("tags." + key)));
        }
        //nick = (data.getString("nick") == null) ? mm.deserialize(data.getString("nick")) : null;
        tag = (data.getString("tag") != null) ? tags.get(data.getString("tag")) : null;
    }

    public UUID getUuid() { return uuid; }

    /*public boolean hasNick() { return nick != tag; }
    public Component getNick() { return nick; }
    public void setNick(Component nick) { this.nick = nick; }*/

    public boolean hasTag() { return tag != null; }
    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }

    public String getChatcolor() { return chatcolor; }
    public void setChatcolor(String chatcolor) { this.chatcolor = chatcolor; }
}