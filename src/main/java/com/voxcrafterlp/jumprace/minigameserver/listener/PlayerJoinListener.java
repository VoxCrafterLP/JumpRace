package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.manager.InventoryManager;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.objects.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.02.2021
 * Time: 19:32
 * Project: JumpRace
 */

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        switch (JumpRace.getInstance().getGameManager().getGameState()) {
            case LOBBY:
                event.setJoinMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("minigameserver-player-join-message", player.getName()));
                if(JumpRace.getInstance().getLocationManager().getLobbyLocation() != null)
                    player.teleport(JumpRace.getInstance().getLocationManager().getLobbyLocation());

                JumpRace.getInstance().getInventoryManager().setInventory(player, InventoryManager.Type.LOBBY);

                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.setLevel(Countdown.Type.LOBBY.getDuration());
                player.setExp(Countdown.Type.LOBBY.getDuration() * ((float) 1 / Countdown.Type.LOBBY.getDuration()));
                player.getInventory().setArmorContents(null);

                JumpRace.getInstance().getGameManager().handlePlayerJoin();
                Bukkit.getOnlinePlayers().forEach(players -> new PlayerScoreboard().setScoreboard(players));
                break;
            case DEATHMATCH:
            case JUMPING:
                JumpRace.getInstance().getGameManager().getSpectatorManager().setSpectating(player);
                event.setJoinMessage(null);
                break;
        }
    }
}
