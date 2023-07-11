package me.phill310.cavern.objects;

import net.kyori.adventure.text.Component;

public class Tag {
    private final long unlocked;
    private final String name;
    private Component description;
    private Component display;
    private long created;

    public Tag(String name, long unlocked) {
        this.unlocked = unlocked;
        this.name = name;
    }

    public long getUnlocked() {
        return unlocked;
    }

    public String getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public Component getDisplay() {
        return display;
    }

    //TODO: Make a way to convert tags to a storeable object
}
