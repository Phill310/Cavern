package me.phill310.cavern.objects;

import me.phill310.cavern.Main;
import me.phill310.cavern.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Manages the storage of profiles, moving data between files and CACHE
 */
public class ProfileManager implements Listener {

    private static final HashMap<UUID, Profile> profiles = new HashMap<>();
    private static final Main plugin = Main.getPlugin(Main.class);
    private static File profileFolder;

    /**
     * Setup files and folders to make sure they all exist
     */
    public static void setup() {
        profiles.clear();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }


        profileFolder = new File(plugin.getDataFolder(), "userdata");



        if (!profileFolder.exists()) {
            try {
                profileFolder.mkdir();
                Bukkit.getLogger().info("The profile folder has been created");
            } catch (Exception e) {
                Bukkit.getLogger().info("Could not create the profile folder");
            }
        }
    }

    /**
     * Save all profiles from CACHE to its configuration file
     */
    public static void saveAll() {
        for (Profile profile : profiles.values()) {
            saveProfile(profile, true);
        }
    }

    /**
     * Get a {@link Profile} from configuration file, or CACHE if loaded
     * @param uuid uuid of the profile to be fetched
     * @return {@link Profile}
     */
    public static Profile loadProfile(UUID uuid) {
        if (profiles.containsKey(uuid)) return profiles.get(uuid);
        File playerFile = new File(profileFolder, uuid + ".yml");
        Profile profile = null;
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                profile = new Profile(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profile = new Profile(YamlConfiguration.loadConfiguration(playerFile));
        }
        profiles.put(uuid, profile);
        return profile;
    }

    /**
     * Save a {@link Profile} to CACHE
     * @param profile profile to be saved
     */
    public static void saveProfile(Profile profile) {
        profiles.put(profile.getUuid(), profile);
    }

    /**
     * Save a profile to:<br>
     * - config file (toFile = true)<br>
     * - CACHE (toFile = false)
     * @param profile profile to be saved
     * @param toFile should profile be saved to config file
     */
    public static void saveProfile(Profile profile, boolean toFile) {
        profiles.put(profile.getUuid(), profile);
        if (toFile) {
            //Bukkit.broadcast(Component.text("starting saving process"));
            File profileFolder = new File(plugin.getDataFolder(), "userdata");
            File playerFile = new File(profileFolder, profile.getUuid() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("uuid", profile.getUuid().toString());
            if (profile.hasSelectedTag()) {
                config.set("tag", profile.getSelectedTag().getName());
            }
            for (Tag tag : profile.getTags().values()) {
                config.set("tags." + tag.getName(), tag.getUnlocked());
            }
            config.set("chatcolor", profile.getChatcolor());
            if (profile.getColors().size() != 0) {
                config.set("colors", profile.getColors());
            }
            if (profile.isBold()) config.set("bold", true);

            try {
                config.save(playerFile);
                //Bukkit.broadcast(Component.text("Saved the file"));
            } catch (IOException e) {
                Utils.log("It fucked up");
                e.printStackTrace();
                //throw new RuntimeException(e);
            }
        }

    }

    /**
     * Remove a profile from CACHE
     * @param uuid uuid of profile to be removed
     */
    public static void unloadProfile(UUID uuid) {
        profiles.remove(uuid);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        saveProfile(loadProfile(uuid), true);
        unloadProfile(uuid);
    }

}