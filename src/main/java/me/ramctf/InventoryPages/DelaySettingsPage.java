package me.ramctf.InventoryPages;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ramctf.GameProperties;
import me.ramctf.InventorySlots.InventorySlotsDelaySelection;
import net.md_5.bungee.api.ChatColor;

public class DelaySettingsPage implements Listener{
    static String inventoryName = "Delay Settings";
    static int[] timeOptions = {0, 5, 10, 15, 20, 25, 35, 45, 60};

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e){
        if(e.getView().getTitle().equals(inventoryName)){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();
            int slot = e.getSlot();

            if(clickedItem == null || clickedItem.getType() == Material.AIR){
                return;
            } 
            
            GameProperties.setPregameDuration(timeOptions[slot]);
            p.sendMessage(ChatColor.GREEN + "Pregame Timer set to " + GameProperties.pregameTimer() + " minutes");
            GameSettingsPage.ShowHomePage(p);

        }
    }

    public static void ShowHomePage(Player p){
        Inventory inv = Bukkit.createInventory(p, 9, inventoryName);
        
        for(int i = 0; i < 9; i++){
            inv.setItem(i, InventorySlotsDelaySelection.getTimeOptions(i, timeOptions));
        }

        p.openInventory(inv);
    }
}


