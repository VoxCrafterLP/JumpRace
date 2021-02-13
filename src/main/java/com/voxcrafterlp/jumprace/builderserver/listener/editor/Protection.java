package com.voxcrafterlp.jumprace.builderserver.listener.editor;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

}
