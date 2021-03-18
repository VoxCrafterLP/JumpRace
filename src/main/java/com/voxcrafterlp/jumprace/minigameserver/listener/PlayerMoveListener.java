package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 18.03.2021
 * Time: 19:09
 * Project: JumpRace
 */

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(JumpRace.getInstance().getGameManager().getPreJumpingCountdown().isRunning()
                || JumpRace.getInstance().getGameManager().getPreDeathMatchCountdown().isRunning())
            event.setCancelled(true);
    }

}
