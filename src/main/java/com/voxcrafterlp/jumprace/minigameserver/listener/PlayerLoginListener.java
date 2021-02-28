package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 27.02.2021
 * Time: 19:50
 * Project: JumpRace
 */

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, JumpRace.getInstance().getPrefix() + "§7The game has §calready §7started§8!");
        }
    }

}
