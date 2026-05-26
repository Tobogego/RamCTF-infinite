package me.ramctf;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class FlagLogic implements Listener{
    
    private static int captureDistance = 1;

    public static void calculateCaptures(){
        for(Player p: Bukkit.getOnlinePlayers()){
            if(playerCanStealThisFlag(p, "Red")){
                playerStoleRedFlagTasks(p);
            }
            if(playerCanStealThisFlag(p, "Blue")){
                playerStoleBlueFlagTasks(p);
            }
            if(playerCanRecoverThisFlag(p, "Red")){
                playerRecoveredRedFlagTasks(p);
            }
            if(playerCanRecoverThisFlag(p, "Blue")){
                playerRecoveredBlueFlagTasks(p);
            }
            if(playerCanCaptureThisFlag(p, "Red")){
                playerCapturedRedFlagTasks(p);
            }
            if(playerCanCaptureThisFlag(p, "Blue")){
                playerCapturedBlueFlagTasks(p);
            }

        }
    }

    public static void spawnBlueFlag(Location loc){
        loc.getBlock().setType(org.bukkit.Material.BLUE_BANNER);
    }

    public static void spawnRedFlag(Location loc){
        loc.getBlock().setType(org.bukkit.Material.RED_BANNER);
    }

    public static void removeBlueFlag(Location loc){
        if (loc != null){
            loc.getBlock().setType(org.bukkit.Material.AIR);
        }
    }

    public static void removeRedFlag(Location loc){
        if (loc != null){
            loc.getBlock().setType(org.bukkit.Material.AIR);
        }
    }

    private static boolean playerCanRecoverThisFlag(Player p, String team){
        if(p.isDead() || p.getGameMode() == GameMode.SPECTATOR){
            return false;
        }

        if(team.equalsIgnoreCase("Blue")){
            if(Teams.getTeam(p).equalsIgnoreCase("Blue")){
                if(GameProperties.blueFlagOnGround()){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.blueFlagCurrentLocation(), captureDistance)){
                        if(!GameProperties.blueFlagLocationBase().equals(GameProperties.blueFlagCurrentLocation())){
                            return true;
                        }
                    }
                }
            }
        }
        if(team.equalsIgnoreCase("Red")){
            if(Teams.getTeam(p).equalsIgnoreCase("Red")){
                if(GameProperties.redFlagOnGround()){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.redFlagCurrentLocation(), captureDistance)){
                        if(!GameProperties.redFlagLocationBase().equals(GameProperties.redFlagCurrentLocation())){
                            return true;
                        }
                    }
                }
            }
         }

        return false;
    }

    

    private static boolean playerCanStealThisFlag(Player p, String flag){
        if(p.isDead() || p.getGameMode() == GameMode.SPECTATOR){
            return false;
        }

        if(flag.equalsIgnoreCase("Blue")){
            if(Teams.getTeam(p).equalsIgnoreCase("Red")){
                if(GameProperties.blueFlagOnGround()){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.blueFlagCurrentLocation(), captureDistance)){
                        return true;
                    }
                }
            }
        }
            
        if(flag.equalsIgnoreCase("Red")){
            if(Teams.getTeam(p).equalsIgnoreCase("Blue")){
                if(GameProperties.redFlagOnGround()){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.redFlagCurrentLocation(), captureDistance)){
                        return true;
                    }
                }
            }
        }
        return false;
        
    }

    private static boolean playerCanCaptureThisFlag(Player p, String flag){
        if(p.isDead() || p.getGameMode() == GameMode.SPECTATOR){
            return false;
        }

        if(flag.equalsIgnoreCase("Blue")){
            if(Teams.getTeam(p).equalsIgnoreCase("Red")){
                if(p.equals(GameProperties.blueFlagCarrier())){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.redFlagLocationBase(), captureDistance)){
                        return true;
                    }
                }
            }
        } 
        if(flag.equalsIgnoreCase("Red")){
            if(Teams.getTeam(p).equalsIgnoreCase("Blue")){
                if(p.equals(GameProperties.redFlagCarrier())){
                    if(Helpers.withinDistance(p.getLocation(), GameProperties.blueFlagLocationBase(), captureDistance)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void playerRecoveredRedFlagTasks(Player p){
        removeRedFlag(GameProperties.redFlagCurrentLocation());
        GameProperties.setRedFlagCurrentLocation(GameProperties.redFlagLocationBase());
        p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN + " returned your flag", "Red");
        Teams.broadcastMessage(p.getName() + ChatColor.RED + " returned enemy flag", "Blue");
    }

    private static void playerRecoveredBlueFlagTasks(Player p){
        removeBlueFlag(GameProperties.blueFlagCurrentLocation());
        GameProperties.setBlueFlagCurrentLocation(GameProperties.blueFlagLocationBase());
        p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN + " returned your flag", "Blue");
        Teams.broadcastMessage(p.getName() + ChatColor.RED + " returned enemy flag", "Red");
        GameManager.endGame();
    }

    private static void playerStoleRedFlagTasks(Player p){
        removeRedFlag(GameProperties.redFlagCurrentLocation());
        GameProperties.setRedFlagCarrier(p);
        GameProperties.setRedFlagCurrentLocation(null);
        GameProperties.setRedFlagOnGround(false);
        p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
        Teams.playAlarm("Red");
        Teams.broadcastMessage(p.getName() + ChatColor.RED + " stole your flag", "Red");
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN +  " stole enemy flag", "Blue");
        GameManager.endGame();
    }

    private static void playerStoleBlueFlagTasks(Player p){
        removeBlueFlag(GameProperties.blueFlagCurrentLocation());
        GameProperties.setBlueFlagCarrier(p);
        GameProperties.setBlueFlagCurrentLocation(null);
        GameProperties.setBlueFlagOnGround(false);
        p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
        Teams.playAlarm("Blue");
        Teams.broadcastMessage(p.getName() + ChatColor.RED +  " stole your flag", "Blue", p);
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN + " stole enemy flag", "Red", p);
    }

    private static void playerCapturedRedFlagTasks(Player p){
        GameProperties.setRedFlagCarrier(null);
        GameProperties.setRedFlagCurrentLocation(GameProperties.redFlagLocationBase());
        GameProperties.setRedFlagOnGround(true);
        GameProperties.setBlueTeamScore(GameProperties.blueTeamScore() + 1);
        Teams.playVictory("Blue");
        p.removePotionEffect(PotionEffectType.GLOWING);
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN + " captured enemy flag", "Blue");
        Teams.broadcastMessage(p.getName() + ChatColor.RED + " captured your flag", "Red");
    }

    private static void playerCapturedBlueFlagTasks(Player p){
        GameProperties.setBlueFlagCarrier(null);
        GameProperties.setBlueFlagCurrentLocation(GameProperties.blueFlagLocationBase());
        GameProperties.setBlueFlagOnGround(true);
        GameProperties.setRedTeamScore(GameProperties.redTeamScore() + 1);
        Teams.playVictory("Red");
        p.removePotionEffect(PotionEffectType.GLOWING);
        Teams.broadcastMessage(p.getName() + ChatColor.GREEN + " captured enemy flag", "Red");
        Teams.broadcastMessage(p.getName() + ChatColor.RED + " captured your flag", "Blue");
    }

}
