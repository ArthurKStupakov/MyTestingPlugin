package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        if (strings.length < 2) return false;

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
            return false; //if target == config && action != reset
        }

        if(target.equalsIgnoreCase("radio")){

            if(action.equalsIgnoreCase("add")){
                int countargs = strings.length;
                String message = "";

                for (int i = 2; i < countargs; i++){
                    message = message + " " + strings[i];
                }

                File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
                FileConfiguration msg = YamlConfiguration.loadConfiguration(config);

                List<String> list = msg.getStringList("messages." + plugin.active_lang + ".broadcast_radio");
                if (!list.contains(message)) {
                    list.add(message);

                    msg.set("messages." + plugin.active_lang + ".broadcast_radio", list);
                    try {
                        msg.save(config);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                plugin.reloadConfig();
                return true;
            }
            if(action.equalsIgnoreCase("clearall")){

                File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
                FileConfiguration msg = YamlConfiguration.loadConfiguration(config);

                List<String> list = new ArrayList<>();
                msg.set("messages." + plugin.active_lang + ".broadcast_radio", list);

                try {
                    msg.save(config);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                plugin.reloadConfig();
                return true;
            }

            return false; //if target == radio && action != add or clearall
        }


        return false; //if target == config or radio
    }
}
