package me.phill310.cavern.ore;

import me.phill310.cavern.Main;
import me.phill310.cavern.Utils;
import me.phill310.cavern.objects.Ore;
import me.phill310.cavern.objects.OreManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!OreManager.isOre(block.getType())) return;
        Ore ore = OreManager.getOre(block.getType());
        Player player = event.getPlayer();
        event.setCancelled(true);
        block.setType(Material.BEDROCK);
        if (ore.hasDrop()) {
            Utils.give(player, ore.getDrop());
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> block.setType(ore.getType()), (long) ore.getCooldown()*20);
    }

}
