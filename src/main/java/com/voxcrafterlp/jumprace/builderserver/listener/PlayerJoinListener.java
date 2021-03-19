package com.voxcrafterlp.jumprace.builderserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:49
 * Project: JumpRace
 */

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("builderserver-player-join-message", event.getPlayer().getName()));
    }

}
