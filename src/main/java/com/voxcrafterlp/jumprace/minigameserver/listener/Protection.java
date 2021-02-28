package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 28.02.2021
 * Time: 12:29
 * Project: JumpRace
 */

public class Protection implements Listener {

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.ARENA)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.ARENA)
            event.setCancelled(true);
    }

}
