/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropRollInfo {
    private static ConcurrentHashMap<UUID,DropRollRecord> records = new ConcurrentHashMap<>();
    
    public static void recordRoll(TrophyRollEvent event){
        if(!(event.getMiner() instanceof Player)) return;
        DropRollRecord record = new DropRollRecord(event);
        records.put(event.getMiner().getUniqueId(), record);
    }    
    public static void recordRoll(HeadRollEvent event){
        if(!(event.getKiller() instanceof Player)) return;
        DropRollRecord record = new DropRollRecord(event);
        records.put(event.getKiller().getUniqueId(), record);
    }
    
    
    //Example command handler
    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("trophystatistics.lastroll")) {
            sender.sendMessage("You do not have permission for this command.");
            return true;
        }
        
        if(!(sender instanceof Player)){
            sender.sendMessage("Command must be ran by a player.");
            return true;
        }
        
        Player player = (Player) sender;
        
        DropRollRecord record = records.get(player.getUniqueId());
        
        if(record==null){
            sender.sendMessage("Sorry, I could not find a drop roll for you.");
            return true;
        }
        
        sender.sendMessage(record.toString());

        //Do stuff
        //return false;
        return true;
    }
}
