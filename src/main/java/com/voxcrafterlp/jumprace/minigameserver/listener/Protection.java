package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
    public void onPlace(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.ARENA)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.LOBBY || JumpRace.getInstance().getGameManager().getGameState() == GameState.ENDING)
            event.setCancelled(!(event.getPlayer().getGameMode() == GameMode.CREATIVE));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING || JumpRace.getInstance().getGameManager().getGameState() == GameState.ARENA) return;
        if(event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }

}
