package me.phill310.cavern.guis.master;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class PaginatedGUI extends GUI{
    public PaginatedGUI(String identifier, Component title, GUIManager manager) {
        super(identifier, 54, title);
        this.manager = manager;
    }

    private int page = 0;
    private final LinkedList<Entry> allEntries = new LinkedList<>();
    private final GUIManager manager;

    /**
     * Get what number page is open<br>
     * GUIs start on page 0
     * @return page number
     */
    public int getPageNumber() {
        return page;
    }

    /**
     * Get how many pages this gui has
     * @return number greater than 0
     */
    public int getLastPage() {
        return Math.max((int) Math.ceil(allEntries.size()/45.0), 1);
    }

    /**
     * Turn to the next page of the gui<br>
     * Will automatically populate the inventory with new items
     * @param inventory inventory to populate
     */
    public void nextPage(Inventory inventory) {
        if (allEntries.size() >= (page+1)*45) page++;
        generatePage();
        loadPage(inventory);
    }
    /**
     * Turn to the previous page of the gui<br>
     * Will automatically populate the inventory with new items
     * @param inventory inventory to populate
     */
    public void previousPage(Inventory inventory) {
        if (page > 0) page--;
        generatePage();
        loadPage(inventory);
    }

    /**
     * Move the required entries from {@link PaginatedGUI#getAllEntries()} into {@link GUI#getEntries()} so that they can be displayed by {@link GUIManager#openGUI(Player, GUI)}
     */
    public void generatePage() {
        int start = (45*page);
        int end = start+44;
        if (allEntries.size() < end) end = allEntries.size() - 1;
        HashMap<Integer, Entry> entries = super.getEntries();
        entries.clear();
        for (int slot = start; slot <= end; slot++) {
            Entry entry = allEntries.get(slot);
            entry.setSlot(slot-start);
            entries.put(slot-start,entry);
        }
        for (int i = 45; i < 54; i++) {
            entries.put(i, new GlassEntry(i));
        }
        if (allEntries.size() >= (page+1)*45) entries.put(52, new ArrowEntry(Material.ARROW, "<blue>Next (Page " + (page+2) + ")", null, 52, this));
        if (page > 0) entries.put(46, new ArrowEntry(Material.ARROW, "<blue>Back (Page " + (page) + ")", null, 46, this));
        entries.put(49, new ArrowEntry(Material.PAPER, "<blue>Page " + (page+1) + "/" + getLastPage(), null, 49, this));
    }

    /**
     * Updated the contents of the inventory to display a new page
     * @param inventory inventory to update
     */
    public void loadPage(Inventory inventory) {
        inventory.clear();
        manager.updateGUI(inventory, this);
    }

    /**
     * Get a list of all the entries in the PaginatedGUI
     * @return list of entries
     */
    public LinkedList<Entry> getAllEntries() {
        return allEntries;
    }

    /**
     * Add an entry to the PaginatedGUI
     * @param entry entry to be added
     */
    @Override
    public void addEntry(Entry entry) {
        allEntries.add(entry);
    }

    /**
     * Remove an entry from the PaginatedGUI
     * @param entry entry to be removed
     */
    @Override
    public void removeEntry(Entry entry) {
        allEntries.remove(entry);
    }
}
