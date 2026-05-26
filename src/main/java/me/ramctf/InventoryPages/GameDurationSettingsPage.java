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
import me.ramctf.InventorySlots.InventorySlotsGameTimerSelection;
import net.md_5.bungee.api.ChatColor;

public class GameDurationSettingsPage implements Listener{
    static String inventoryName = "Game Duration";
    static int[] timeOptions = {5, 10, 15, 20, 25, 30, 40, 50, 60, -1};

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
            
            GameProperties.setGameDuration(timeOptions[slot]);
            String durationMessage = timeOptions[slot] == -1 ? "Infinite" : timeOptions[slot] + " minutes";
            p.sendMessage(ChatColor.GREEN + "Game duration set to " + durationMessage);
            GameSettingsPage.ShowHomePage(p);

        }
    }

    public static void ShowHomePage(Player p){

        Inventory inv = Bukkit.createInventory(p, 18, inventoryName);
        for(int i = 0; i < timeOptions.length; i++){
            inv.setItem(i, InventorySlotsGameTimerSelection.getTimeOptions(i, timeOptions));
        }

        p.openInventory(inv);
        p.openInventory(inv);
    }
}


