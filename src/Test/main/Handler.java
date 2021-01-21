package Test.main;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Handler implements Listener {
    private MyTestingPlugin plugin;
    private List<String> colors = new ArrayList<String>();
    private List<String> colored_blocks = new ArrayList<String>();

    public Handler(MyTestingPlugin myPlugin) {
        plugin = myPlugin;

        colors.add("WHITE");
        colors.add("RED");
        colors.add("GREEN");
        colors.add("BLUE");
        colors.add("BLACK");
        colors.add("ORANGE");
        colors.add("MAGENTA");
        colors.add("YELLOW");
        colors.add("LIME");
        colors.add("PINK");
        colors.add("GRAY");
        colors.add("CYAN");
        colors.add("PURPLE");
        colors.add("BROWN");

        colored_blocks.add("WOOL");
        colored_blocks.add("TERRACOTTA");
        colored_blocks.add("CONCRETE");
        colored_blocks.add("GLASS");
        colored_blocks.add("CARPET");
        colored_blocks.add("SHULKER");
        colored_blocks.add("BANNER");
    }

    @EventHandler
    public void join(PlayerJoinEvent event) { //join messages and add to players log(players.yml)
        Player player = event.getPlayer();
        List<String> joinMessages = plugin.getConfig().getStringList("messages." + plugin.active_lang + ".join");

        Random rand = new Random();
        String randMessage = joinMessages.get(rand.nextInt(joinMessages.size()));

        player.sendMessage(plugin.messageCorrect(randMessage, player.getName()));

        File players = new File(plugin.getDataFolder() + File.separator + "players.yml");
        FileConfiguration users = YamlConfiguration.loadConfiguration(players);
        List<String> list = users.getStringList("users");
        if (!list.contains(player.getName())) {
            list.add(player.getName());
            users.set("users", list);
            try {
                users.save(players);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String privateInventoryName = plugin.getConfig().getString("messages." + plugin.active_lang + ".private_inventory");
        privateInventoryName = plugin.messageCorrect(privateInventoryName, player.getName()).toUpperCase(Locale.ROOT);

        Inventory privateInventory = Bukkit.createInventory(null, 3 * 9, privateInventoryName);
        plugin.getPrivateInventoryMap().putIfAbsent(player, privateInventory);

        return;

    }

    @EventHandler
    public void chest_placed(PlayerInteractEvent event) { //deny to players open chest when chest item in hands
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.CHEST) {
            if (player.isSneaking()) return;
            else if (event.getClickedBlock().getType() == Material.CHEST) {
                event.setCancelled(true);
                String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".chest_setter");
                player.sendMessage(message);
            }
        }
        return;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockReplacedState().getType() != Material.AIR) return;

        if (event.getBlock().getType() == Material.IRON_BLOCK) {  //delete iron blocks in 4sec
            Runnable runnableForIronBlock = new Runnable() {
                @Override
                public void run() {
                    event.getBlock().setType(Material.AIR);
                }
            };
            Bukkit.getScheduler().runTaskLater(plugin, runnableForIronBlock, 40);
            return;
        }

        Location location = event.getBlockPlaced().getLocation();
        Block block = location.getBlock();

        final String[] block_name = {String.valueOf(block.getType())};

        for (String block_type : colored_blocks) {  //changes color of colored blocks one times in one sec
            int idBlock = block_name[0].indexOf(block_type);
            if (idBlock != -1) {


                Location current_location = location;
                final int[] id = {0};

                Runnable runnableForChangeColoredBlock = new Runnable() {
                    @Override
                    public void run() {
                        if (current_location.getBlock().getType() == Material.AIR){
                            Bukkit.getScheduler().cancelTask(id[0]);
                            return;
                        }

                        for (String color : colors) {
                            int id = block_name[0].indexOf(color);
                            if (id != -1) {

                                int inPostitonColor = colors.indexOf(color);
                                int inNextPostitonColor = 0;

                                if (colors.size() - 1 > inPostitonColor) {
                                    inNextPostitonColor = inPostitonColor + 1;
                                }

                                block_name[0] = block_name[0].replace("_LIGHT_","_");
                                block_name[0] = block_name[0].replace("LIGHT_","");
                                block_name[0] = block_name[0].replace(color, "" + colors.get(inNextPostitonColor));

                                break;
                            }
                        }
                        block.setType(Material.getMaterial(block_name[0]));
                    }
                };
                id[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnableForChangeColoredBlock, 0, 10 );
                return;
            }

        }
    }
}