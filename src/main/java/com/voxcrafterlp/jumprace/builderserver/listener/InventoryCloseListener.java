package com.voxcrafterlp.jumprace.builderserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 03:35
 * Project: JumpRace
 */

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(ModuleSetup.getActiveSetups().containsKey(player)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> {
                player.openInventory(event.getInventory());
                player.playSound(player.getLocation(), Sound.NOTE_BASS,2,2);
            },2);
        }

        if(event.getInventory().getName() == null) return;

        if(event.getInventory().getName().equals(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-inventory-name"))) {
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 1);
        }
    }
}
