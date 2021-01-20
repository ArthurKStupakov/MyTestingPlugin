package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommendPutMeInFire implements CommandExecutor {
    private MyTestingPlugin plugin;
    public CommendPutMeInFire(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) return false;
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Only for players!");
            return true;
        }

        if (!commandSender.hasPermission("MyTestPlugin.dieBySelf")) {
            commandSender.sendMessage(ChatColor.RED + "You dont have permisson!");
            return true;
        }

        Player player = (Player) commandSender;

        if(player.getInventory().getItemInMainHand().getType() != Material.LAVA_BUCKET) return false;

        player.getInventory().getItemInMainHand().setType(Material.BUCKET);

        player.setFireTicks(99000);
        if (player.getHealth()>5) player.setHealth(5);
        player.setSaturation(0);

        Bukkit.broadcastMessage(player.getName() + " set himself on fire because he thinks he's right!");

        player.sendMessage(ChatColor.RED + "Be Happy!");


        return true;
    }
}
