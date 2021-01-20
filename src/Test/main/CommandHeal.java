package Test.main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {
    private MyTestingPlugin plugin;

    public CommandHeal(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) return false;
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Only for players!");
            return true;
        }

        if (!commandSender.hasPermission("MyTestPlugin.heal")) {
            commandSender.sendMessage(ChatColor.RED + "You dont have permisson!");
            return true;
        }

        Player player = (Player) commandSender;
        player.setHealth(20);
        player.sendMessage(ChatColor.GOLD + "Be Happy!");

        return true;
    }
}
