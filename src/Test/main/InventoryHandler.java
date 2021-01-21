package Test.main;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryHandler implements Listener {
    private final MyTestingPlugin plugin;

    public InventoryHandler(MyTestingPlugin myPlugin) {
        plugin = myPlugin;

    }

    @EventHandler
    public void handle(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        Inventory overallInventory = plugin.getOverall_inventory();
        Inventory inventory_clicked = event.getClickedInventory();


        if (inventory.equals(overallInventory)) {
            if (inventory_clicked == null) return; //cursor did not hit the inventory field

            if (inventory_clicked.equals(overallInventory) && (event.getCursor().getType() == Material.AIR)){
                event.setCancelled(true);
        }
        }
    }
}
