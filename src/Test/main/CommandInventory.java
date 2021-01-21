package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandInventory implements CommandExecutor {
    private MyTestingPlugin plugin;
    public CommandInventory(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length != 1) return false;

        if (!(commandSender instanceof Player)){
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".only_for_players");
            commandSender.sendMessage(message);
            return true;
        }

        if (!commandSender.hasPermission("MyTestPlugin.inventory")) {
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".permission_error");
            commandSender.sendMessage(message);
            return true;
        }

        Player player = (Player) commandSender;
        String inventory_name = strings[0];

        if(inventory_name.equalsIgnoreCase("overall")) {
            Inventory inventory = plugin.getOverall_inventory();
            player.openInventory(plugin.getOverall_inventory());

            return true;
        }

        Player target_player = Bukkit.getPlayer(inventory_name);
        if (target_player==null){
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".player_offline");
            commandSender.sendMessage(plugin.messageCorrect(message, inventory_name));
            return true;
        }

        if (plugin.getPrivateInventoryMap().containsKey(player)){
            Inventory privateInventory = (Inventory) plugin.getPrivateInventoryMap().get(player);
            if (privateInventory != null){
                player.openInventory(privateInventory);
                return true;
            }

            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".inventory_not_found");
            commandSender.sendMessage(plugin.messageCorrect(message));
        } else {
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".inventory_not_found");
            commandSender.sendMessage(plugin.messageCorrect(message));
        }
        return true;
    }
}
