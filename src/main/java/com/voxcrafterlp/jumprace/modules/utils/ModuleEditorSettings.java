package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.EditorMode;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 18.02.2021
 * Time: 12:52
 * Project: JumpRace
 */

@Getter @Setter
public class ModuleEditorSettings {

    private final ModuleEditor moduleEditor;
    private final Player player;
    private Inventory settingsInventory;

    private EditorMode editorMode;

    public ModuleEditorSettings(ModuleEditor moduleEditor) {
        this.moduleEditor = moduleEditor;
        this.player  = moduleEditor.getPlayer();

        this.editorMode = EditorMode.QUICK;
        this.buildInventory();
    }

    /**
     * Build the settings inventory
     */
    private void buildInventory() {
        this.settingsInventory = Bukkit.createInventory(null, 27, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-inventory-name"));

        for(int i = 0; i<27; i++)
            this.settingsInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.settingsInventory.setItem(10, new ItemManager(Material.NAME_TAG).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-rename-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-rename-description")).build());
        this.settingsInventory.setItem(12, new ItemManager(Material.ANVIL).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-change-difficulty-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-change-difficulty-description")).build());
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-quick-editor") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-performance-editor")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-editor-description")).build());
        this.settingsInventory.setItem(16, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-save-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-save-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UyYTUzMGY0MjcyNmZhN2EzMWVmYWI4ZTQzZGFkZWUxODg5MzdjZjgyNGFmODhlYThlNGM5M2E0OWM1NzI5NCJ9fX0="));
        this.settingsInventory.setItem(26, new ItemManager(Material.BARRIER).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-cancel-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-cancel-description")).build());
    }

    /**
     * Update the editor item in the inventory
     */
    public void updateInventory() {
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-quick-editor") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-performance-editor")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-editor-description")).build());
    }

    /**
     * Open the settings inventory and play a CHEST_OPEN sound
     */
    public void openInventory() {
        this.player.openInventory(this.settingsInventory);
        this.player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10, 10);
    }

}
