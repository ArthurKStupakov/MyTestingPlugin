package Test.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class MyTestingPlugin extends JavaPlugin{

    Logger log = Logger.getLogger("Minecraft");
    public String active_lang;

    private final Map<Player, Inventory> privateInventory = new HashMap<>();
    private final Inventory overall_inventory = Bukkit.createInventory(null, 3 * 9, "messages." + active_lang + ".overall_inv_title");


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

        log.info("[TEST] info.");
        log.warning("[TEST] warning!");
        log.severe("[TEST] severe!!");

        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        Bukkit.getPluginManager().registerEvents(new EnderTeleportHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(this), this);

        getCommand("myplugin").setExecutor(new CommandMyPlugin(this));
        getCommand("info").setExecutor(new CommandInfo(this));
        getCommand("heal").setExecutor(new CommandHeal(this));
        getCommand("putmeinfire").setExecutor(new CommandPutMeInFire(this));
        getCommand("inventory").setExecutor(new CommandInventory(this));
        craft();

        //Run broadcast radio
        Runnable runnableBroadcastRadio = new Runnable() {
            @Override
            public void run() {
                while(isEnabled()){
                    List<String> radioMessages = getConfig().getStringList("messages." + active_lang + ".broadcast_radio");
                    if (radioMessages.size() > 0 ) {


                        for (Player player : Bukkit.getOnlinePlayers()) {

                            radioMessages = getConfig().getStringList("messages." + active_lang + ".broadcast_radio");
                            Random rand = new Random();
                            String randMessage = radioMessages.get(rand.nextInt(radioMessages.size()));

                            player.sendMessage(messageCorrect(randMessage, player.getName()));
                            log.info(randMessage);
                        }
                        try {
                            Thread.sleep(getConfig().getInt("radio_time_rate"));
                        }
                        catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }

                }
            }

        };
        Bukkit.getScheduler().runTaskAsynchronously(this,runnableBroadcastRadio);

        //create overall inventory
        File inventories = new File(getDataFolder() + File.separator + "inventories.yml");
        if(!inventories.exists()){
            try {
                inventories.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ItemStack item_food = new ItemStack(Material.COOKED_BEEF);
        item_food.setAmount(8);
        overall_inventory.setItem(0,item_food);

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

    public Inventory getOverall_inventory(){
        return overall_inventory;
    }

    public Map getPrivateInventoryMap(){
        return privateInventory;
    }
}
