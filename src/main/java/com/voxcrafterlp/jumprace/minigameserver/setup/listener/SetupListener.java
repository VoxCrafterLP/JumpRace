package com.voxcrafterlp.jumprace.minigameserver.setup.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.setup.objects.MapSetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.02.2021
 * Time: 17:09
 * Project: JumpRace
 */

public class SetupListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if(!JumpRace.getInstance().getMapSetups().containsKey(player)) return;

        final MapSetup mapSetup = JumpRace.getInstance().getMapSetups().get(player);
        event.setCancelled(true);

        switch (event.getMessage().toLowerCase()) {
            case "add":
                mapSetup.addSpawnLocation(player.getLocation().clone());
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-add-success"));
                break;
            case "endpoint":
                mapSetup.addEndPointLocation(player.getLocation().clone());
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-endpoint-success"));
                break;
            case "finish":
                if(!mapSetup.finish()) return;

                JumpRace.getInstance().getLocationManager().saveData();
                JumpRace.getInstance().getLocationManager().loadData();
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-finish-success"));
                break;
            default:
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-commands"));
                break;
        }

    }
}
