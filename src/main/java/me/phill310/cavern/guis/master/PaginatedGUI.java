package me.phill310.cavern.guis.master;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
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

    public int getPageNumber() {
        return page;
    }

    public int getLastPage() {
        return (int) Math.ceil(allEntries.size()/45.0);
    }

    public void nextPage(Inventory inventory) {
        if (allEntries.size() >= (page+1)*45) page++;
        generatePage();
        loadPage(inventory);
    }

    public void previousPage(Inventory inventory) {
        if (page > 0) page--;
        generatePage();
        loadPage(inventory);
    }

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

    public void loadPage(Inventory inventory) {
        inventory.clear();
        manager.updateGUI(inventory, this);
    }

    public LinkedList<Entry> getAllEntries() {
        return allEntries;
    }

    @Override
    public void addEntry(Entry entry) {
        allEntries.add(entry);
    }
    @Override
    public void removeEntry(Entry entry) {
        allEntries.remove(entry);
    }
}
