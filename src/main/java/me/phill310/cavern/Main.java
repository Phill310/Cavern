package me.phill310.cavern;

import me.phill310.cavern.chat.PlayerChat;
import me.phill310.cavern.chat.commands.CommandTag;
import me.phill310.cavern.objects.OreManager;
import me.phill310.cavern.objects.ProfileManager;
import me.phill310.cavern.objects.TagManager;
import me.phill310.cavern.ore.BlockBreak;
import me.phill310.cavern.ore.commands.CommandOres;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    private static Chat chat = null;
    private static Economy econ = null;

    public static final Logger log = Logger.getLogger("Minecraft");

    public final PluginManager manager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();

        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault/Economy Plugin dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupChat()) {
            log.severe(String.format("[%s] - Disabled due to no Vault/Permission Plugin dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        //chat
        manager.registerEvents(new PlayerChat(), this);
        CommandTag commandTag = new CommandTag();
        manager.registerEvents(commandTag, this);
        this.getCommand("tag").setExecutor(commandTag);


        //ore
        manager.registerEvents(new BlockBreak(), this);
        CommandOres ores = new CommandOres();
        manager.registerEvents(ores, this);
        this.getCommand("ore").setExecutor(ores);

        //misc
        this.getCommand("cavern").setExecutor(new CommandCavern());
        manager.registerEvents(new ProfileManager(), this);

        log.info("We Gucci");
        ProfileManager.setup();
        OreManager.setup();
        TagManager.setup();

        //TODO: save the profiles every x minutes
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ProfileManager.saveAll();
        OreManager.saveAll();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Chat getChat() {
        return chat;
    }
}
