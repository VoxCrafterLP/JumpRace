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
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build())) {
                        session.getEditorSetup().getBorders()[0] = event.getBlock().getLocation();
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-succuss-1"));
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
                case 1:
                    if(player.getItemInHand() == null) return;
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build())) {
                        session.getEditorSetup().getBorders()[1] = event.getBlock().getLocation();
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-succuss-2"));
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
                case 2:
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build())) {
                        session.getModule().setStartPointLocation(event.getBlock().getLocation());
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-succuss-3"));
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
                case 3:
                    if(event.getPlayer().getItemInHand().equals(new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build())) {
                        session.getModule().setEndPointLocation(event.getBlock().getLocation());
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-succuss-4"));
                        session.getEditorSetup().nextStep();
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }

}
