package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.manager.InventoryManager;
import com.voxcrafterlp.jumprace.objects.Countdown;
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
                event.setJoinMessage(JumpRace.getInstance().getPrefix() + "§a" + player.getName() + " §7joined the §bgame§8.");
                if(JumpRace.getInstance().getLocationManager().getLobbyLocation() != null)
                    player.teleport(JumpRace.getInstance().getLocationManager().getLobbyLocation());

                JumpRace.getInstance().getInventoryManager().setInventory(player, InventoryManager.Type.LOBBY);

                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.setLevel(Countdown.Type.LOBBY.getDuration());
                player.setExp(Countdown.Type.LOBBY.getDuration() * ((float) 1 / Countdown.Type.LOBBY.getDuration()));

                JumpRace.getInstance().getGameManager().handlePlayerJoin();
                break;
            case ARENA:
            case JUMPING:

                break;
        }
    }
}
