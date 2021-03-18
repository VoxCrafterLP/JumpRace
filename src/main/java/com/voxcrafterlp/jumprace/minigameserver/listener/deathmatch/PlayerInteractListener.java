package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
        }

        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;

        if(event.getItem().getType() == Material.COMPASS) {
            final List<Entity> nearbyEntities = player.getNearbyEntities(100.0D, 100.0D, 100.0D);
            final AtomicReference<Player> nearestPlayer = new AtomicReference<>();

            if(nearbyEntities.isEmpty()) {
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§cNo enemy could be found!");
                return;
            }

            nearbyEntities.forEach(entity -> {
                if(entity instanceof Player) {
                    final Player target = (Player) entity;
                    if(nearestPlayer.get() != null) {
                        if(player.getLocation().distance(target.getLocation()) > nearestPlayer.get().getLocation().distance(target.getLocation()))
                            nearestPlayer.set(target);
                    } else
                        nearestPlayer.set(target);
                }
            });

            if(nearestPlayer.get().isEmpty()) {
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§cNo enemy could be found!");
                return;
            }

            int distance = Math.round((float) nearestPlayer.get().getLocation().distance(player.getLocation()));
            player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Tracked player§8: " +
                    JumpRace.getInstance().getGameManager().getPlayerNames().get(nearestPlayer.get()) +
                    " §7Blocks away: §3" + distance);
            player.setCompassTarget(nearestPlayer.get().getLocation());
        }
    }
}
