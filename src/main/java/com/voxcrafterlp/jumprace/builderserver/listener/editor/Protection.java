package com.voxcrafterlp.jumprace.builderserver.listener.editor;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 13.02.2021
 * Time: 16:42
 * Project: JumpRace
 */

public class Protection implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(JumpRace.getInstance().getEditorSessions().containsKey(event.getPlayer())) {
            final ItemStack drop = event.getItemDrop().getItemStack();

            if(drop.getItemMeta() == null) return;
            if(drop.getItemMeta().getDisplayName() == null) return;
            if(drop.getItemMeta().getDisplayName().equals("§cSettings"))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(JumpRace.getInstance().getEditorSessions().containsKey((Player) event.getWhoClicked())) {
            if(event.getCurrentItem() == null) return;
            final ItemStack drop = event.getCurrentItem();

            if(drop.getItemMeta() == null) return;
            if(drop.getItemMeta().getDisplayName() == null) return;
            if(drop.getItemMeta().getDisplayName().equals("§cSettings"))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(JumpRace.getInstance().getEditorSessions().containsKey((Player) event.getPlayer())) {
            if(event.getItemInHand() == null) return;
            final ItemStack item = event.getItemInHand();

            if(item.getItemMeta() == null) return;
            if(item.getItemMeta().getDisplayName() == null) return;
            if(item.getItemMeta().getDisplayName().equals("§cSettings"))
                event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        event.setCancelled(JumpRace.getInstance().getEditorSessions().containsKey((Player) event.getEntity()));
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            if(block.getType() == Material.GOLD_BLOCK) {
                JumpRace.getInstance().getEditorSessions().forEach((player, session) -> {
                    if(session.getModule().getSpawnLocation() != null) {
                        if(session.getModule().getStartPointLocation().equals(block.getLocation()))
                            Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> block.setType(Material.GOLD_BLOCK), 1);
                    }
                });
            }
        });
    }

    @EventHandler
    public void onExtend(BlockPistonExtendEvent event) {
        if(event.getBlocks().stream().filter(block -> block.getType().equals(Material.GOLD_BLOCK)).count() != 0) {
            JumpRace.getInstance().getEditorSessions().forEach((player, session) -> {
                if(session.getModule().getSpawnLocation() != null) {
                    if(event.getBlocks().stream().filter(block -> block.getLocation().equals(session.getModule().getStartPointLocation())).count() != 0)
                        event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent event) {
        if(event.getBlocks().stream().filter(block -> block.getType().equals(Material.GOLD_BLOCK)).count() != 0) {
            JumpRace.getInstance().getEditorSessions().forEach((player, session) -> {
                if(session.getModule().getSpawnLocation() != null) {
                    if(event.getBlocks().stream().filter(block -> block.getLocation().equals(session.getModule().getStartPointLocation())).count() != 0)
                        event.setCancelled(true);
                }
            });
        }
    }
}
