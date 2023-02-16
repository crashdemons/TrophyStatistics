/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import com.github.crashdemons.trophystatistics.modifiers.DropRateModifier;
import com.github.crashdemons.trophystatistics.modifiers.DropRateModifierType;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropRollRecord {    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public final DropRollType type;
    
    //PH properties
    private final Map<String, DropRateModifier> modifiers;
    private Entity targetEntity;
    
    //MT properties
    private Block targetBlock;
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
        
        targetBlock = event.getTarget();
        
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
        String str = "Drop Roll: "+type.name()+ " "+(type==DropRollType.HEAD ? targetEntity.getName() : targetBlock.getType().name())+"\n"
                + "  Original Droprate: " + originalDropRate + "\n"
                + "  Original Roll: "+originalDropRoll+" (lower is better)\n";
        if(modifiers.size()>0 || alwaysRewarded){
            str += "  Modifiers:"+"\n";
            for(Map.Entry<String,DropRateModifier> modifierEntry : modifiers.entrySet()){
                String name = modifierEntry.getKey();
                DropRateModifier mod = modifierEntry.getValue();
                str += "    "+name+": "+mod.getType().name()+" "+mod.getLevel()+"x "+mod.getValue()+"\n";
            }
            if(alwaysRewarded) str += "    User is Always Rewarded\n";
        }
        //str += "  (Luck doesn't show but affects the effective rate)\n";
        str += "  Effective Droprate: "+effectiveDropRate+"\n";
        str += "  Effective Roll: "+effectiveDropRoll+" (lower is better)\n";
        str += " Roll was successful? "+(dropSuccess?"YES":"NO");
       
        return str;
    }
}
