package com.voxcrafterlp.jumprace.modules.objects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.EditorMode;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.02.2021
 * Time: 14:44
 * Project: JumpRace
 */

@Getter
public class EditorSetup {

    private final ModuleEditor moduleEditor;
    private final Player player;
    private int step;

    private final Location[] borders;

    public EditorSetup(ModuleEditor moduleEditor) {
        this.moduleEditor = moduleEditor;
        this.step = (this.moduleEditor.getSettings().getEditorMode() == EditorMode.QUICK) ? 3 : 0;
        this.player = moduleEditor.getPlayer();
        this.borders = new Location[2];
    }

    public void startSetup() {
        this.sendInstructions();
    }

    public void nextStep() {
        this.step++;

        if(this.step == 4) {
            this.moduleEditor.exitEditor();
            return;
        }

        this.sendInstructions();
    }

    private void sendInstructions() {
        switch (this.step) {
            case 0:
                this.player.getInventory().clear();
                this.player.getInventory().setItem(0, new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build());
                this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-instructions-1"));
                break;
            case 1:
                this.player.getInventory().setItem(0, new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build());
                this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-instructions-2"));
                break;
            case 2:
                this.player.getInventory().setItem(0, new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build());
                this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-instructions-3"));
                break;
            case 3:
                this.player.getInventory().setItem(0, new ItemManager(Material.DIAMOND_AXE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("selection-tool-name")).build());
                this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("editor-setup-instructions-4"));
                break;
        }
    }

}
