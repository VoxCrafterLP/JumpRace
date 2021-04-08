package com.voxcrafterlp.jumprace.builderserver.listener.editor;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 08.04.2021
 * Time: 01:24
 * Project: JumpRace
 */

public class BlockPhysicsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhysics(BlockPhysicsEvent event) {
        if(!JumpRace.getInstance().getEditorSessions().isEmpty()) {
            JumpRace.getInstance().getEditorSessions().forEach((player, moduleEditor) -> event.setCancelled(!moduleEditor.getSettings().isEnablePhysics()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlace(BlockPlaceEvent event) {
        if(!JumpRace.getInstance().getEditorSessions().isEmpty()) {
            JumpRace.getInstance().getEditorSessions().forEach((player, moduleEditor) -> {
                if(moduleEditor.getSettings().isEnablePhysics()) return;

                if(!event.isCancelled() && this.isFallingBlock(event.getBlock().getType())) {
                    final Block blockBelow = event.getBlock().getRelative(BlockFace.DOWN);

                    if(blockBelow.getType() == Material.AIR) {
                        blockBelow.setType(Material.BARRIER);
                        event.getBlock().setType(event.getBlock().getType(), false);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> blockBelow.setType(Material.AIR), 1);
                    }
                }
            });
        }
    }

    private boolean isFallingBlock(Material material) {
        switch (material) {
            case SAND:
            case GRAVEL:
            case ANVIL:
            case DRAGON_EGG:
                return true;
            default:
                return false;
        }
    }

}
