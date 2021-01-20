package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMyPlugin implements CommandExecutor {
    private MyTestingPlugin plugin;

    public CommandMyPlugin(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("MyTestPlugin.admin")){
            commandSender.sendMessage(ChatColor.RED + "You dont have permisson!");
            return true;
        }

        if(!(strings.length == 2)){
            return false;
        }

        String target = strings[0];
        String action = strings[1];

        if(target.equalsIgnoreCase("config")){
            if(action.equalsIgnoreCase("reset")){
                plugin.reloadConfig();
                commandSender.sendMessage("Configs reloaded.");
                return true;
            }
            return false;
        }

        return false;
    }
}
