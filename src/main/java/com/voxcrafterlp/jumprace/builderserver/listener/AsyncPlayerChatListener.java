package com.voxcrafterlp.jumprace.builderserver.listener;

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
        final Player player = event.getPlayer();

        if(ModuleSetup.getActiveSetups().containsKey(player)) {
            event.setCancelled(true);
            ModuleSetup setup = ModuleSetup.getActiveSetups().get(player);

            if(setup.getSetupStep() == 0) {
                String[] input = event.getMessage().split(" ");
                if(input.length > 1) {
                    player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("invalid-module-name"));
                    return;
                }
                if(input[0].length() > 14) {
                    player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("module-name-too-long"));
                    return;
                }
                if(input[0].equalsIgnoreCase("default")) {
                    player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("invalid-module-name"));
                    return;
                }

                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("set-module-name-success", input[0]));

                setup.setName(input[0]);
                setup.nextStep();
            } else
            if(setup.getSetupStep() == 1) {
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("set-module-builders-success", event.getMessage()));

                setup.setBuilder(event.getMessage());
                setup.nextStep();
            }
        }
    }
}
