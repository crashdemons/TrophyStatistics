/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import com.github.crashdemons.trophystatistics.modifiers.DropRateModifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropRollRecord {    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    NumberFormat doubleFormat = new DecimalFormat("#0.0000");
    
    public final DropRollType type;
    
    //PH properties
    private final Map<String, DropRateModifier> modifiers;
    private Entity targetEntity;
    
    //MT properties
    private Material targetMaterial;
    //private final double fortuneModifier;
    
    //Shared properties
    private boolean alwaysRewarded;
    private final Entity roller;
    private final double originalDropRoll;
    private final double effectiveDropRoll;
    private final double originalDropRate;
    private final double effectiveDropRate;
    private final boolean dropSuccess;
    
    public DropRollRecord(TrophyRollEvent event){
        type = DropRollType.MINING_TROPHY;
        
        modifiers= new LinkedHashMap<>();
        for(Map.Entry<String,com.github.crashdemons.miningtrophies.modifiers.DropRateModifier> entry : event.getModifiers().entrySet()){
            modifiers.put(entry.getKey(), DropRateModifier.fromMT( entry.getValue() ) );
        }
        
        targetMaterial = event.getTargetMaterial();
        
        alwaysRewarded=event.getMinerAlwaysRewarded();
        roller = event.getMiner();
        originalDropRoll=event.getOriginalDropRoll();
        originalDropRate=event.getOriginalDropRate();
        effectiveDropRoll=event.getEffectiveDropRoll();
        effectiveDropRate=event.getEffectiveDropRate();
        dropSuccess=event.getDropSuccess();
    }
    public DropRollRecord(HeadRollEvent event){
        type = DropRollType.HEAD;
        
        modifiers= new LinkedHashMap<>();
        for(Map.Entry<String,org.shininet.bukkit.playerheads.events.modifiers.DropRateModifier> entry : event.getModifiers().entrySet()){
            modifiers.put(entry.getKey(), DropRateModifier.fromPH( entry.getValue() ) );
        }
        
        targetEntity = event.getTarget();
        
        alwaysRewarded=event.getKillerAlwaysBeheads();
        roller = event.getKiller();
        originalDropRoll=event.getOriginalDropRoll();
        originalDropRate=event.getOriginalDropRate();
        effectiveDropRoll=event.getEffectiveDropRoll();
        effectiveDropRate=event.getEffectiveDropRate();
        dropSuccess=event.getDropSuccess();
    }
    @Override
    public String toString() {
        String str = ChatColor.GOLD+"Drop Roll: "+ChatColor.GREEN+type.name()+ " "+ChatColor.YELLOW+(type==DropRollType.HEAD ? targetEntity.getName() : targetMaterial.name())+"\n"
                + ChatColor.GOLD+"  Original Droprate: "+ChatColor.GREEN+ originalDropRate + ChatColor.GRAY+ChatColor.ITALIC+" (0.0-1.0; roll needs to be under this)\n"
                + ChatColor.GOLD+"  Original Roll: "+ChatColor.GREEN+doubleFormat.format(originalDropRoll)+ChatColor.GRAY+ChatColor.ITALIC+" (0.0-1.0; lower is better)\n";
        if(modifiers.size()>0 || alwaysRewarded){
            str += ChatColor.GOLD+"  Modifiers:"+"\n";
            for(Map.Entry<String,DropRateModifier> modifierEntry : modifiers.entrySet()){
                String name = modifierEntry.getKey();
                DropRateModifier mod = modifierEntry.getValue();
                str += ChatColor.LIGHT_PURPLE+"    "+name+": "+ChatColor.GREEN+mod.getType().name()+ChatColor.YELLOW+" "+mod.getLevel()+"x "+mod.getValue()+"\n";
            }
            if(alwaysRewarded) str += ChatColor.LIGHT_PURPLE+"    User is Always Rewarded\n";
        }
        //str += "  (Luck doesn't show but affects the effective rate)\n";
        str += ChatColor.GOLD+"  Modified Droprate: "+ChatColor.GREEN+effectiveDropRate+ChatColor.GRAY+ChatColor.ITALIC+" (0.0-1.0; roll needs to be under this)\n";
        str += ChatColor.GOLD+"  Modified Roll: "+ChatColor.GREEN+doubleFormat.format(effectiveDropRoll)+ChatColor.GRAY+ChatColor.ITALIC+" (0.0-1.0; lower is better)\n";
        str += ChatColor.GOLD+" Roll was successful? "+(dropSuccess? ChatColor.GREEN+"YES":ChatColor.RED+"NO");
       
        return str;
    }
}
