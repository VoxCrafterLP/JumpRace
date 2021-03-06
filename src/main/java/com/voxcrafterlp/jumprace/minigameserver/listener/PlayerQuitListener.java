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
                event.setQuitMessage(JumpRace.getInstance().getPrefix() + "§a" + player.getName() + " §7left the §bgame§8.");
                Bukkit.getScheduler().scheduleAsyncDelayedTask(JumpRace.getInstance(), () -> JumpRace.getInstance().getGameManager().handlePlayerQuit(player), 1);
                break;
            case DEATHMATCH:
            case JUMPING:

                break;
        }
    }

}
