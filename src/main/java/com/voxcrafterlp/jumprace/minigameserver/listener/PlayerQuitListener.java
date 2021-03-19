package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 21:08
 * Project: JumpRace
 */

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        switch (JumpRace.getInstance().getGameManager().getGameState()) {
            case LOBBY:
                event.setQuitMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("minigameserver-player-quit-messag", player.getName()));
                Bukkit.getScheduler().scheduleAsyncDelayedTask(JumpRace.getInstance(), () -> JumpRace.getInstance().getGameManager().handlePlayerQuit(player), 1);
                break;
            case DEATHMATCH:
            case JUMPING:
                event.setQuitMessage(null);
                Bukkit.getScheduler().scheduleAsyncDelayedTask(JumpRace.getInstance(), () -> JumpRace.getInstance().getGameManager().handlePlayerQuit(player), 1);
                break;
            case ENDING:
                event.setQuitMessage(null);
                break;
        }
    }

}
