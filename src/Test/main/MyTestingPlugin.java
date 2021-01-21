package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MyTestingPlugin extends JavaPlugin{

    Logger log = Logger.getLogger("Minecraft");
    public String active_lang;

    public void onEnable(){

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()){
            getLogger().info("Creating new config files in plugins folder...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            getLogger().info("Done!");
        }
        reloadConfig();
        active_lang = getConfig().getString("language");

        File players = new File(getDataFolder() + File.separator + "players.yml");
        if(!players.exists()){
            try {
                players.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //getLogger().info("Enabled");
        log.info("My plugin enabled");
        log.warning("Oh my GOOOOOD!!");
        log.severe("Be quiet.. This is the END..");

        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        Bukkit.getPluginManager().registerEvents(new EnderTeleportHandler(this), this);

        getCommand("myplugin").setExecutor(new CommandMyPlugin(this));
        getCommand("info").setExecutor(new CommandInfo(this));
        getCommand("heal").setExecutor(new CommandHeal(this));
        getCommand("putmeinfire").setExecutor(new CommandPutMeInFire(this));
        craft();



    }

    @Override
    public void onDisable() {
        log.info("My plugin disabled");
    }

    private void craft(){ //making new crafts (ender-teleport)

        String item_name = messageCorrect(getConfig().getString("messages." + active_lang + ".teleport_item_name"));
        String item_lore = messageCorrect(getConfig().getString("messages." + active_lang + ".teleport_item_lore"));

        ItemStack item = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(item_name);
        List<String> lore = new ArrayList<String>();
        lore.add(item_lore);
        meta.setLore(lore);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "teleporter");

        ShapedRecipe shapedRecipe = new ShapedRecipe(key, item);
        shapedRecipe.shape(new String[] {"A A"," B ","A A"});
        shapedRecipe.setIngredient('A', Material.ENDER_PEARL);
        shapedRecipe.setIngredient('B', Material.ENDER_EYE);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }

    public String messageCorrect(String message){ //is used for format plugin messages
        message = message.replace("&", "\u00a7");
        return message;
    }

    public String messageCorrect(String message, String nickname){
        message = message.replace("&", "\u00a7");
        message = message.replace("{player}", nickname);
        return message;
    }
}
