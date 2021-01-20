package Test.main;



import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Handler implements Listener{
    private MyTestingPlugin plugin;

    public Handler(MyTestingPlugin myPlugin) {
        plugin = myPlugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent event){
        Player player = event.getPlayer();

        //String joinMessage = plugin.getConfig().getString("messages." + plugin.active_lang + ".join");

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
        return;

    }

    @EventHandler
    public void breaking(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        player.sendMessage("Блок сломан: ["+ block.getX() + "," + block.getY() + "," + block.getZ() + "]");
    }

    @EventHandler
    public void chest_placed(PlayerInteractEvent event){
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.CHEST) {
            if (player.isSneaking()) return;
            else if(event.getClickedBlock().getType() == Material.CHEST){
                event.setCancelled(true);
                player.sendMessage("Убери сундук из рук, мен");
            }
        }
        return;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        plugin.log.warning(String.valueOf(event));
        if (event.getBlockReplacedState().getType() != Material.AIR) return;

        if (event.getBlock().getType() == Material.IRON_BLOCK){
            Runnable runnableForIronBlock = new Runnable() {
                @Override
                public void run() {
                    event.getBlock().setType(Material.AIR);
                }
            };
            Bukkit.getScheduler().runTaskLater(plugin, runnableForIronBlock, 40);
        }


    }

}
