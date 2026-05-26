package me.ramctf.EventHandler;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.ramctf.GameProperties;
import me.ramctf.Helpers;

public class BlockInteractHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        int distance = 3;
        if(!GameProperties.flagBuildProtection()){
            distance = 1;
        }

        Block b = e.getBlock();
        int x, z;

        if(GameProperties.blueFlagLocationBase() != null){
            x = Helpers.get3AxisDistance(b.getLocation(), GameProperties.blueFlagLocationBase())[0];
            z = Helpers.get3AxisDistance(b.getLocation(), GameProperties.blueFlagLocationBase())[2];

            if(x < distance && z < distance){
                e.setCancelled(true);
            }
        }

        if(GameProperties.redFlagLocationBase() != null){
            x = Helpers.get3AxisDistance(b.getLocation(), GameProperties.redFlagLocationBase())[0];
            z = Helpers.get3AxisDistance(b.getLocation(), GameProperties.redFlagLocationBase())[2];

            if(x < distance && z < distance){
                e.setCancelled(true);
            }
        }
        
        if(b.getLocation().equals(GameProperties.blueFlagCurrentLocation()) || b.getLocation().equals(GameProperties.redFlagCurrentLocation())){
            e.setCancelled(true);
        }
    
    }

@EventHandler
public void onBlockPlace(BlockPlaceEvent e){
    int distance = 3;
    if(!GameProperties.flagBuildProtection()){
        distance = 1;
    }

    Block b = e.getBlock();
    int x, z, y;

    if(GameProperties.blueFlagLocationBase() != null){
        int[] axis = Helpers.get3AxisDistance(b.getLocation(), GameProperties.blueFlagLocationBase());
        x = axis[0];
        y = axis[1];
        z = axis[2];

        // Only restrict if within distance on X-Z AND at similar height
        if(x < distance && z < distance && y < 3){  // Allow building if Y is 3+ blocks different
            e.setCancelled(true);
        }
    }

    if(GameProperties.redFlagLocationBase() != null){
        int[] axis = Helpers.get3AxisDistance(b.getLocation(), GameProperties.redFlagLocationBase());
        x = axis[0];
        y = axis[1];
        z = axis[2];

        if(x < distance && z < distance && y < 3){
            e.setCancelled(true);
        }
    }
}
}
