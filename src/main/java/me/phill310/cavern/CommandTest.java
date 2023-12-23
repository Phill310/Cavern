package me.phill310.cavern;

import me.phill310.cavern.objects.Pickaxe;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CommandTest implements CommandExecutor {
    private final List<Material> picks = Arrays.asList(Material.WOODEN_PICKAXE,Material.STONE_PICKAXE,Material.IRON_PICKAXE,Material.GOLDEN_PICKAXE,Material.DIAMOND_PICKAXE,Material.NETHERITE_PICKAXE);
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (picks.contains(player.getInventory().getItemInMainHand().getType())) {
                Pickaxe pick = new Pickaxe(player.getInventory().getItemInMainHand().clone());
                if (args.length > 0) {
                    if (args[0].startsWith("e")) pick.addEfficiency();
                    if (args[0].startsWith("f")) pick.addFortune();
                    if (args[0].startsWith("x")) pick.addXpBoost();
                }
                player.getInventory().setItemInMainHand(pick.getPick());
            } else {
                Pickaxe pick = new Pickaxe(player.getUniqueId());
                Utils.give(player, pick.getPick());
            }
        }
        return true;
    }
}
