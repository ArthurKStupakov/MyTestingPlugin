package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPutMeInFire implements CommandExecutor {
    private MyTestingPlugin plugin;
    public CommandPutMeInFire(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    } //give for player chance burn himself

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) return false;
        if (!(commandSender instanceof Player)){
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".only_for_players");
            commandSender.sendMessage(message);
            return true;
        }

        if (!commandSender.hasPermission("MyTestPlugin.dieBySelf")) {
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".permission_error");
            commandSender.sendMessage(message);
            return true;
        }

        Player player = (Player) commandSender;

        if(player.getInventory().getItemInMainHand().getType() != Material.LAVA_BUCKET) return false;
        player.getInventory().getItemInMainHand().setType(Material.BUCKET);
        player.setFireTicks(99000);
        if (player.getHealth()>5) player.setHealth(5);
        player.setSaturation(0);

        String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".fire_suicide");
        Bukkit.broadcastMessage(plugin.messageCorrect(message, player.getName()));
        return true;
    }
}
