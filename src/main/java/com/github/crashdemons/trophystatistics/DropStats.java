/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.trophystatistics;

import com.github.crashdemons.miningtrophies.TrophyType;
import com.github.crashdemons.miningtrophies.events.TrophyRollEvent;
import com.github.crashdemons.playerheads.api.HeadType;
import com.github.crashdemons.playerheads.api.PlayerHeads;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropStats {
    
    private static ScoreboardManager scoreboardManager = null;
    private static Scoreboard scoreboard = null;
    private static Objective objective = null;
    
    //private static ConcurrentHashMap<HeadType,String> HeadScores = new ConcurrentHashMap<>();
    //private static ConcurrentHashMap<TrophyType,String> TrophyScores = new ConcurrentHashMap<>();

    
    private static String getScoreUser(TrophyType mt){
        return "MiningTrophies:"+mt.name();
    }
    private static String getScoreUser(HeadType ht){
        return "PlayerHeads:"+ht.toEnum().name();
    }
    
    public static void prepareScores(){
        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getMainScoreboard();
        objective = scoreboard.getObjective("TrophyDropTotals");
        if(objective==null) objective = scoreboard.registerNewObjective("TrophyDropTotals", Criteria.DUMMY, "TrophyDropTotals");
        
        /*
        HeadType[] heads = PlayerHeads.getApiInstance().getSupportedHeads();
        for(HeadType ht : heads){
            HeadScores.put(ht, getScoreUser(ht));
        }
        for(TrophyType mt : TrophyType.values()){
            TrophyScores.put(mt, getScoreUser(mt));
        }*/
    } 
    
    public static void applyStatChanges(TrophyRollEvent event){
        if(!event.succeeded()) return;
        Material mat = event.getTargetMaterial();
        TrophyType mt = TrophyType.get(mat);
        if(mt==null) return;
        String username = getScoreUser(mt);
        Score score = objective.getScore(username);
        score.setScore(score.getScore()+1);
    }
    
    public static void applyStatChanges(HeadRollEvent event){
        if(!event.succeeded()) return;
        Entity e = event.getTarget();
        if(e==null) return;
        HeadType ht = PlayerHeads.getApiInstance().getHeadOf(e);
        if(ht==null) return;
        String username = getScoreUser(ht);
        Score score = objective.getScore(username);
        score.setScore(score.getScore()+1);
    }
}
