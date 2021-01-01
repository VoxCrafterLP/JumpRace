package com.voxcrafterlp.jumprace.builderserver.listener.setup;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 03:25
 * Project: JumpRace
 */

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getInventory() == null) return;
        if(event.getInventory().getName() == null) return;
        if(!event.getInventory().getName().equals("§cModule difficulty")) return;
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(ModuleSetup.getActiveSetups().containsKey(player)) {
            ModuleSetup setup = ModuleSetup.getActiveSetups().get(player);

            if(setup.getSetupStep() != 2) return;

            switch (event.getSlot()) {
                case 10:
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §aEASY§7.");
                    setup.setModuleDifficulty(ModuleDifficulty.EASY);
                    setup.nextStep();

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                    player.closeInventory();
                    break;
                case 12:
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §6NORMAL§7.");
                    setup.setModuleDifficulty(ModuleDifficulty.NORMAL);
                    setup.nextStep();

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                    player.closeInventory();
                    break;
                case 14:
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §cHARD§7.");
                    setup.setModuleDifficulty(ModuleDifficulty.HARD);
                    setup.nextStep();

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                    player.closeInventory();
                    break;
                case 16:
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §4VERY HARD§7.");
                    setup.setModuleDifficulty(ModuleDifficulty.VERY_HARD);
                    setup.nextStep();

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                    player.closeInventory();
                    break;
                default:
                    player.playSound(player.getLocation(), Sound.NOTE_BASS,1,1);
                    break;
            }

            event.setCancelled(true);
        }
    }

}
