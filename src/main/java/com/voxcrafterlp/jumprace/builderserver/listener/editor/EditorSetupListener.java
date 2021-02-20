package com.voxcrafterlp.jumprace.builderserver.listener.editor;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.02.2021
 * Time: 15:01
 * Project: JumpRace
 */

public class EditorSetupListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(JumpRace.getInstance().getEditorSessions().containsKey(event.getPlayer())) {
            final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(event.getPlayer());
            final Player player = event.getPlayer();

            if(session.getEditorSetup() == null) return;

            switch(session.getEditorSetup().getStep()) {
                case 0:
                    if(player.getItemInHand() == null) return;
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName("§bSelection tool").build())) {
                        session.getEditorSetup().getBorders()[0] = event.getBlock().getLocation();
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The §blower left corner §7has been set §asuccessfully§8.");
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
                case 1:
                    if(player.getItemInHand() == null) return;
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName("§bSelection tool").build())) {
                        session.getEditorSetup().getBorders()[1] = event.getBlock().getLocation();
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The §bupper right corner §7has been set §asuccessfully§8.");
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
                case 2:
                    session.getModule().setStartPointLocation(event.getBlock().getLocation());
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The §bstart point location §7has been set §asuccessfully§8.");
                    session.getEditorSetup().nextStep();
                    event.setCancelled(true);
                    break;
                case 3:
                    session.getModule().setEndPointLocation(event.getBlock().getLocation());
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The §bend point location §7has been set §asuccessfully§8.");
                    session.getEditorSetup().nextStep();
                    event.setCancelled(true);
                    break;
            }
        }
    }

}
