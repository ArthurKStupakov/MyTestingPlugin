package Test.main;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnderTeleportHandler implements Listener {
    private MyTestingPlugin plugin;
    private List<Material> void_blocks = new ArrayList<Material>();


    public EnderTeleportHandler(MyTestingPlugin myPlugin){
        plugin = myPlugin;

        void_blocks.add(Material.AIR);
        void_blocks.add(Material.GRASS);
        void_blocks.add(Material.LAVA);
        void_blocks.add(Material.OXEYE_DAISY);
        void_blocks.add(Material.DANDELION);
        void_blocks.add(Material.POPPY);
        // And so on...
    } //make ender-teleport working

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.ENDER_EYE) return;
        Player player = event.getPlayer();

        if(player.getCooldown(Material.ENDER_EYE)!= 0) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasDisplayName()) return;
        if(!item.getItemMeta().hasLore()) return;

        String item_name = plugin.getConfig().getString("messages." + plugin.active_lang + ".teleport_item_name");
        String item_lore = plugin.getConfig().getString("messages." + plugin.active_lang + ".teleport_item_lore");

        if(!item.getItemMeta().getDisplayName().equals(plugin.messageCorrect(item_name))) return;
        if(!item.getItemMeta().getLore().get(0).equals(plugin.messageCorrect(item_lore))) return;

        event.setCancelled(true);
        teleport(player);
    }

    private void removeItem(Player player){
        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);
    }

    private void teleport(Player player){

        float range = 2;
        float vertical_mod = 1;

        Location location = player.getLocation();
        double pitch = location.getPitch();
        if (pitch >= 50) pitch = -1.0;
        else if (pitch <= -50) pitch = 1.0;
        else pitch = 0.0;

        double x = player.getFacing().getDirection().getX();
        double z = player.getFacing().getDirection().getZ();

        x *= range;
        double y = pitch * (range + vertical_mod);
        z *= range;

        if (y != 0.0) {
            x = 0.0;
            z = 0.0;
        }

        location.setX(location.getX() + x );
        location.setY(location.getY() + y );
        location.setZ(location.getZ() + z );

        if(blocked(location)) {
            String message = plugin.getConfig().getString("messages." + plugin.active_lang + ".teleport_item_wall_error");
            player.sendMessage(message);
            return;
        }

        removeItem(player);
        setEffect(player);
        player.teleport(location);
        player.setCooldown(Material.ENDER_EYE, 40);
        return;

    }

    private boolean blocked(Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Material topBlock = location.getWorld().getBlockAt(x,y,z).getType();
        Material bottomBlock = location.getWorld().getBlockAt(x,y,z).getType();

        if (void_blocks.contains(topBlock) && void_blocks.contains(bottomBlock)) return false;
        return true;
    }

    private void setEffect(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 8, 1);
        player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
    }
}
