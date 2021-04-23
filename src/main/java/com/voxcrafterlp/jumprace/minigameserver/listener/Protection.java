package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 28.02.2021
 * Time: 12:29
 * Project: JumpRace
 */

public class Protection implements Listener {

    private final List<Location> placedCobwebs;

    public Protection() {
        this.placedCobwebs = Lists.newCopyOnWriteArrayList();
    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.DEATHMATCH) {
            if(event.getBlock().getType() == Material.WEB && this.placedCobwebs.contains(event.getBlock().getLocation())) {
                this.placedCobwebs.remove(event.getBlock().getLocation());
                event.getBlock().getDrops().clear();
                return;
            }
        }

        if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.DEATHMATCH) {
            final ItemStack itemInHand = event.getItemInHand();
            if(itemInHand != null)
                if(itemInHand.getType() == Material.WEB || itemInHand.getType() == Material.TNT) {
                    if(itemInHand.getType() == Material.WEB)
                        this.placedCobwebs.add(event.getBlock().getLocation());
                    return;
                }
        }

        if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator((Player) event.getEntity())) {
            event.setCancelled(true);
            return;
        }
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.LOBBY || JumpRace.getInstance().getGameManager().getGameState() == GameState.ENDING)
            event.setCancelled(!(event.getPlayer().getGameMode() == GameMode.CREATIVE));
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator((Player) event.getWhoClicked())) {
            event.setCancelled(true);
            return;
        }
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING || JumpRace.getInstance().getGameManager().getGameState() == GameState.DEATHMATCH) return;
        if(event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
