package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInfo implements CommandExecutor {
    private MyTestingPlugin plugin;


    public CommandInfo(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("MyTestPlugin.info")){
            commandSender.sendMessage(ChatColor.RED + "You dont have permisson!");
            return true;
        }
        if(strings.length == 0) return false;
        String name = strings[0];
        Player player = Bukkit.getPlayer(name);
        if (player==null){
            commandSender.sendMessage(name + " is not online");
            return true;
        }
        commandSender.sendMessage("IP: " + player.getAddress().getAddress());


        return true;
    }
}
