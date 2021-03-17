package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 15:33
 * Project: JumpRace
 */

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) return;

        if(event.getInventory() == null) return;
        if(event.getInventory().getName() == null) return;
        if(event.getCurrentItem() == null) return;

        if(event.getInventory().getName().startsWith("§c✝")) {
            event.setCancelled(true);

            JumpRace.getInstance().getGameManager().getDeathChests().forEach(deathChest -> {
                if(deathChest.getInventory().equals(event.getClickedInventory()))
                    deathChest.takeItem(player, event.getCurrentItem());
            });
        }
    }
}
