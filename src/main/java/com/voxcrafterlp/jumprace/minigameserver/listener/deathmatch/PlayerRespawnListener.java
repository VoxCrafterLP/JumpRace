package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 12:47
 * Project: JumpRace
 */

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.DEATHMATCH)
            event.setRespawnLocation(JumpRace.getInstance().getLocationManager().getSelectedMap().getRandomSpawnLocation());
        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.ENDING)
            event.setRespawnLocation(JumpRace.getInstance().getLocationManager().getLobbyLocation());
    }

}
