package me.phill310.cavern.objects;

import net.kyori.adventure.text.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tag {
    private long unlocked;
    private String unlockedFormatted;
    private final String name;
    private final Component description;
    private final Component display;
    private final long created;
    private final String createdFormatted;
    //private long number;

    public Tag(String name, Component description, Component display, long created, String createdFormatted) {
        this.name = name;
        this.description = description;
        this.display = display;
        this.created = created;
        this.createdFormatted = createdFormatted;
    }

    /*public Tag(String name, long unlocked) {
        this.name = name;
        this.unlocked = unlocked;
    }*/

    public long getUnlocked() {
        return unlocked;
    }
    public void setUnlocked(long unlocked) {
        this.unlocked = unlocked;
        this.unlockedFormatted = (new SimpleDateFormat("MM/dd/yyyy")).format(new Date(unlocked));
    }
    public String getUnlockedFormatted() {
            return unlockedFormatted;
    }
    public long getCreated() { return created; }
    public String getCreatedFormatted() { return createdFormatted; }

    public String getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public Component getDisplay() {
        return display;
    }
}
