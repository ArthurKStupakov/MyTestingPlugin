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
    }  //heal player


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) return false;
        if (!(commandSender instanceof Player)){

            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".only_for_players");
            commandSender.sendMessage(message);
            return true;
        }

        if (!commandSender.hasPermission("MyTestPlugin.heal")) {
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".permission_error");
            commandSender.sendMessage(message);
            return true;
        }

        Player player = (Player) commandSender;
        player.setHealth(20);
        String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".command_done");
        player.sendMessage(message);

        return true;
    }
}
