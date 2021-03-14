package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 10:15
 * Project: JumpRace
 */

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) return;
            JumpRace.getInstance().getGameManager().removeLive(event.getEntity());
    }

}
