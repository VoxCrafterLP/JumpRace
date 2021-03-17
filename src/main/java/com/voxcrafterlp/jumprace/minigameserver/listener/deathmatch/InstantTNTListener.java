package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 17.03.2021
 * Time: 09:18
 * Project: JumpRace
 */

public class InstantTNTListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) {
            event.setCancelled(true);
            return;
        }

        if(event.getBlock().getType() != Material.TNT) return;

        event.getBlock().setType(Material.AIR);
        event.getBlock().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class).setFuseTicks(45);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
    }

}
