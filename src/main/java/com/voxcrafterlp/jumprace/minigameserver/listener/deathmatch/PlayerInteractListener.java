package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 13:01
 * Project: JumpRace
 */

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Player player = event.getPlayer();

        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if(event.getClickedBlock() != null) {
            if(event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                JumpRace.getInstance().getGameManager().getDeathChests().forEach(deathChest -> {
                    if(deathChest.getLocation().equals(event.getClickedBlock().getLocation())) {
                        event.setCancelled(true);
                        player.closeInventory();
                        deathChest.openInventory(player);
                    }
                });
            }
            if(event.getClickedBlock().getType() == Material.BEACON) {
                event.setCancelled(true);
                return;
            }
        }

        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;

        if(event.getItem().getType() == Material.COMPASS) {
            final List<Entity> nearbyEntities = player.getNearbyEntities(100.0D, 100.0D, 100.0D);

            //Removes every entity that is not an instance of Player
            nearbyEntities.forEach(entity -> {
                if(!(entity instanceof Player))
                    nearbyEntities.remove(entity);
            });

            final Player nearestPlayer = (Player) nearbyEntities.stream().sorted((entity1, entity2) -> {
                final double distance1 = player.getLocation().distance(entity1.getLocation());
                final double distance2 = player.getLocation().distance(entity2.getLocation());

                if(distance1 == distance2) return 0;
                return (distance1 < distance2) ? -1 : 1;
            }).findAny().orElse(null);


            if(nearestPlayer == null) {
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-tracker-no-player-found"));
                return;
            }

            final int distance = Math.round((float) nearestPlayer.getLocation().distance(player.getLocation()));
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-tracker-track", JumpRace.getInstance().getGameManager().getPlayerNames().get(nearestPlayer), String.valueOf(distance)));
            player.setCompassTarget(nearestPlayer.getLocation());
        }
    }
}
