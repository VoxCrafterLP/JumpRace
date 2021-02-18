package com.voxcrafterlp.jumprace.builderserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 18.02.2021
 * Time: 12:50
 * Project: JumpRace
 */

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        if(event.getItem().getItemMeta().getDisplayName() == null) return;
        if(!JumpRace.getInstance().getEditorSessions().containsKey(player)) return;

        if(event.getItem().getItemMeta().getDisplayName().equals("§cSettings"))
            JumpRace.getInstance().getEditorSessions().get(player).getSettings().openInventory();
    }
}
