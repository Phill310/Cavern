package me.phill310.cavern.objects;

import net.kyori.adventure.text.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a chat tag (suffix). Includes:<br>
 * - {@link Tag#getUnlocked()}<br>
 * - {@link Tag#getName()}<br>
 * - {@link Tag#getDescription()}<br>
 * - {@link Tag#getDisplay()}<br>
 * - {@link Tag#getCreated()}
 */
public class Tag {
    private long unlocked;
    private String unlockedFormatted;
    private final String name;
    private final Component description;
    private final Component display;
    private final long created;
    private final String createdFormatted;
    private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    //private long number;

    /**
     * Define a tag, explicitly listing its characteristics
     * @param name name of the tag
     * @param description some info about the tag; how it can be obtained per se
     * @param display how the tag appears in chat
     * @param created unix timestamp for when it was created (milliseconds)
     */
    public Tag(String name, Component description, Component display, long created) {
        this.name = name;
        this.description = description;
        this.display = display;
        this.created = created;
        this.createdFormatted = df.format(new Date(created));
    }

    /*public Tag(String name, long unlocked) {
        this.name = name;
        this.unlocked = unlocked;
    }*/

    /**
     * Get the unix time when a parent profile unlocked this tag
     * @return unix time obtained (milliseconds)
     */
    public long getUnlocked() {
        return unlocked;
    }

    /**
     * Set the unix time that the parent profile unlocked this tag
     * @param unlocked new time of unlock
     */
    public void setUnlocked(long unlocked) {
        this.unlocked = unlocked;
        this.unlockedFormatted = df.format(new Date(unlocked));
    }

    /**
     * Get the formatted version of the time that the parent profile unlocked this tag
     * @return unlock time in the form MM/dd/yyyy
     */
    public String getUnlockedFormatted() {
            return unlockedFormatted;
    }
    /**
     * Get the unix time when this tag was created
     * @return unix time created (milliseconds)
     */
    public long getCreated() { return created; }
    /**
     * Get the formatted version of the time that this tag was created
     * @return creation time in the form MM/dd/yyyy
     */
    public String getCreatedFormatted() { return createdFormatted; }

    /**
     * Get the name of this tag
     * @return name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of this tag
     * @return description of this tag
     */
    public Component getDescription() {
        return description;
    }

    /**
     * Get the chat representation of the tag as a component
     * @return chat component of tag
     */
    public Component getDisplay() {
        return display;
    }
}
