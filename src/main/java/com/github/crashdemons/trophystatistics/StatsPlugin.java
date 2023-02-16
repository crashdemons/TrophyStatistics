/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;
import org.shininet.bukkit.playerheads.events.LivingEntityDropHeadEvent;

/**
 * The main plugin class for StatsPlugin
 *
 * @author crash
 */
public class StatsPlugin extends JavaPlugin implements Listener {
    public StatsPlugin instance = null;
    
    private ConcurrentHashMap<UUID,DropRollRecord> records = new ConcurrentHashMap<>();
    
    
    
    
    @Override
    public void onLoad() {
        instance = this;
        //Do Stuff here
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        //Do Stuff here
        //if(getConfig().getBoolean("dothething")){ }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled.");

    }

    @Override
    public void onDisable() {
        //saveConfig();

        //Cleanup here
        getLogger().info("Disabled.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTrophyRollHigh(TrophyRollEvent event){
        if(!(event.getMiner() instanceof Player)) return;
        DropRollRecord record = new DropRollRecord(event);
        records.put(event.getMiner().getUniqueId(), record);
        
        
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeadRollHigh(HeadRollEvent event){
        if(!(event.getKiller() instanceof Player)) return;
        DropRollRecord record = new DropRollRecord(event);
        records.put(event.getKiller().getUniqueId(), record);
    }
    
    
    
    //Example command handler
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("lastroll")) {
            return false;
        }
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
