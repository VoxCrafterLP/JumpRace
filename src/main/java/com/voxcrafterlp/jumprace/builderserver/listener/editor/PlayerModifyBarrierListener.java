package com.voxcrafterlp.jumprace.builderserver.listener.editor;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.InteractionType;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 13.02.2021
 * Time: 20:58
 * Project: JumpRace
 */

public class PlayerModifyBarrierListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(JumpRace.getInstance().getEditorSessions().containsKey(event.getPlayer())) {
            final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(event.getPlayer());
            final Location[] moduleBorders = session.getModule().getModuleBorders();

            if(!isInRegion(event.getBlock().getLocation(), moduleBorders[0], moduleBorders[1])) {
                if(event.getBlock().getLocation().getBlockX() < session.getModule().calculateLocation(session.getModule().getSpawnLocation(), session.getModule().getStartPoint()).getBlockX()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(JumpRace.getInstance().getPrefix() + "§7You can't build §chere§8!");
                    return;
                }
                session.updateBorders(moduleBorders, event.getBlock().getLocation(), InteractionType.PLACE);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(JumpRace.getInstance().getEditorSessions().containsKey(event.getPlayer())) {
            final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(event.getPlayer());
            final Location[] moduleBorders = session.getModule().getModuleBorders();

            if(isInRegion(event.getBlock().getLocation(), moduleBorders[0], moduleBorders[1])) {
                if(event.getBlock().getLocation().equals(session.getModule().getStartPointLocation())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(JumpRace.getInstance().getPrefix() + "§7You can't §cbreak §7the spawn block§7!");
                    return;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> {
                    session.updateBorders(moduleBorders, event.getBlock().getLocation(), InteractionType.BREAK);
                }, 1);
            }
        }
    }

    private boolean isInRegion(Location location, Location border1, Location border2) {
        int x1 = Math.min(border1.getBlockX(), border2.getBlockX());
        int y1 = Math.min(border1.getBlockY(), border2.getBlockY());
        int z1 = Math.min(border1.getBlockZ(), border2.getBlockZ());
        int x2 = Math.max(border1.getBlockX(), border2.getBlockX());
        int y2 = Math.max(border1.getBlockY(), border2.getBlockY());
        int z2 = Math.max(border1.getBlockZ(), border2.getBlockZ());

        return location.getX() >= x1 && location.getX() <= x2 && location.getY() >= y1 && location.getY() <= y2 && location.getZ() >= z1 && location.getZ() <= z2;
    }

}
