package com.voxcrafterlp.jumprace.builderserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.02.2021
 * Time: 15:35
 * Project: JumpRace
 */

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(JumpRace.getInstance().getPrefix() + "§a" +  event.getPlayer().getName() + " §7left the game§8.");
    }

}
