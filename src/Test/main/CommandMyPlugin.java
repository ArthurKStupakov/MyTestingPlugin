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
    } //give plugin usage commands

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("MyTestPlugin.admin")){
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".permission_error");
            commandSender.sendMessage(message);
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
                plugin.active_lang = plugin.getConfig().getString("language");
                String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".configs_reloaded");
                commandSender.sendMessage(message);
                return true;
            }
            return false;
        }

        return false;
    }
}
