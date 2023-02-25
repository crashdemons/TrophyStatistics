/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 * The main plugin class for StatsPlugin
 *
 * @author crash
 */
public class StatsPlugin extends JavaPlugin implements Listener {
    public StatsPlugin instance = null;
    
    
    @Override
    public void onLoad() {
        instance = this;
        //Do Stuff here
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        
        DropStats.prepareScores();
        
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
        DropRollInfo.recordRoll(event);
        DropStats.applyStatChanges(event);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeadRollHigh(HeadRollEvent event){
        DropRollInfo.recordRoll(event);
        DropStats.applyStatChanges(event);
    }
    
    
    
    //Example command handler
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lastroll")) {
            return DropRollInfo.onCommand(sender,cmd,label,args);
        }
        return false;
    }

}
