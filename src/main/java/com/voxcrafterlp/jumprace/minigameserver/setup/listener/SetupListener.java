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
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7A §bspawn location §7has been saved §asuccessfully§8.");
                break;
            case "endpoint":
                mapSetup.addEndPointLocation(player.getLocation().clone());
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7An §bend point location §7has been saved §asuccessfully§8.");
                break;
            case "finish":
                JumpRace.getInstance().getLocationManager().getLoadedMaps().add(mapSetup.finish());
                JumpRace.getInstance().getLocationManager().saveData();
                JumpRace.getInstance().getLocationManager().loadData();
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The map has been §asaved §7successfully§8.");
                break;
            default:
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Valid commands are§8: §badd§8, §bendpoint§8, §bfinish");
                break;
        }

    }
}
