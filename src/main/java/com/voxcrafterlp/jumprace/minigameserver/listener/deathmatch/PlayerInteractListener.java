package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.objects.DeathChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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

        final Player player = event.getPlayer();

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
        if(event.getItem().getItemMeta().getDisplayName() == null) return;

        if(event.getItem().getType() == Material.COMPASS) {

        }

        if(event.getItem().getType() == Material.WORKBENCH) {

        }

        if(event.getItem().getType() == Material.ENCHANTMENT_TABLE) {

        }
    }
}
