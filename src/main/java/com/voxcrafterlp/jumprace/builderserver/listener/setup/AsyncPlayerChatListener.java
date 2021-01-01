package com.voxcrafterlp.jumprace.builderserver.listener.setup;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 02:28
 * Project: JumpRace
 */

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(ModuleSetup.getActiveSetups().containsKey(player)) {
            ModuleSetup setup = ModuleSetup.getActiveSetups().get(player);

            if(setup.getSetupStep() == 0) {
                String[] input = event.getMessage().split(" ");
                if(input.length > 1) {
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§cInvalid name!");
                    return;
                }
                if(input[0].length() > 14) {
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§cThe name is too long!");
                    return;
                }

                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Module name has been set to: §a" + input[0]);

                setup.setName(input[0]);
                setup.nextStep();
            }
            if(setup.getSetupStep() == 1) {
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Module builder(s) has/have been set to: §a" + event.getMessage());

                setup.setBuilder(event.getMessage());
                setup.nextStep();
            }

            event.setCancelled(true);
        }
    }
}
