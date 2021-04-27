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

import java.util.Arrays;
import java.util.List;

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

    private Inventory headsInventory;
    private int headInventoryPage, maxHeadInventoryPages;

    private EditorMode editorMode;
    private boolean enablePhysics;

    public ModuleEditorSettings(ModuleEditor moduleEditor) {
        this.moduleEditor = moduleEditor;
        this.player  = moduleEditor.getPlayer();

        this.editorMode = EditorMode.QUICK;
        this.enablePhysics = true;
        this.buildInventory();
    }

    /**
     * Build the settings inventory
     */
    private void buildInventory() {
        this.settingsInventory = Bukkit.createInventory(null, 45, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-inventory-name"));

        for(int i = 0; i<this.settingsInventory.getSize(); i++)
            this.settingsInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.settingsInventory.setItem(10, new ItemManager(Material.NAME_TAG).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-rename-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-rename-description")).build());
        this.settingsInventory.setItem(12, new ItemManager(Material.ANVIL).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-change-difficulty-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-change-difficulty-description")).build());
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-quick-editor") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-performance-editor")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-editor-description")).build());
        this.settingsInventory.setItem(16, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-save-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-save-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UyYTUzMGY0MjcyNmZhN2EzMWVmYWI4ZTQzZGFkZWUxODg5MzdjZjgyNGFmODhlYThlNGM5M2E0OWM1NzI5NCJ9fX0="));

        this.settingsInventory.setItem(28, new ItemManager(Material.SAND, 1).setDisplayName((this.enablePhysics) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-physics-name-enabled") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-physics-name-disabled")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-physics-description")).build());
        this.settingsInventory.setItem(30, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-heads-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-heads-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThiYzhmYTcxNmNhZGQwMDRiODI4Y2IyN2NjMGY2ZjZhZGUzYmU0MTUxMTY4OGNhOWVjZWZmZDE2NDdmYjkifX19"));
        this.settingsInventory.setItem(32, new ItemManager(Material.NETHER_STAR).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-particles-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-particles-description")).build());

        this.settingsInventory.setItem(44, new ItemManager(Material.BARRIER).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-cancel-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-cancel-description")).build());

        //========================================//

        this.headsInventory = Bukkit.createInventory(null, 54, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("heads-inventory-name"));
        this.headInventoryPage = 1;

        this.maxHeadInventoryPages = JumpRace.getInstance().getJumpRaceConfig().getHeadValues().size() / 36;
        if((JumpRace.getInstance().getJumpRaceConfig().getHeadValues().size() % 36) != 0)
            this.maxHeadInventoryPages++;

        this.buildHeadInventory();

        //========================================//
    }

    /**
     * Update the editor item in the inventory
     */
    public void updateInventory() {
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-quick-editor") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-performance-editor")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-editor-description")).build());
        this.settingsInventory.setItem(28, new ItemManager(Material.SAND, 1).setDisplayName((this.enablePhysics) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-physics-name-enabled") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-physics-name-disabled")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("settings-item-physics-description")).build());
    }

    /**
     * Open the settings inventory and play a CHEST_OPEN sound
     */
    public void openInventory() {
        this.player.openInventory(this.settingsInventory);
        this.player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10, 10);
    }

    /**
     * Toggles the physics and updates the settings inventory
     */
    public void togglePhysics() {
        this.enablePhysics = !enablePhysics;
        player.sendMessage(JumpRace.getInstance().getPrefix() + ((this.enablePhysics) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("message-enable-physics") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("message-disable-physics")));
        this.updateInventory();
    }

    public void switchHeadInventoryPage(int newPage) {
        this.headInventoryPage = newPage;
        this.headsInventory.clear();
        this.buildHeadInventory();
    }

    private void buildHeadInventory() {
        final List<String> headValues = JumpRace.getInstance().getJumpRaceConfig().getHeadValues();

        for(int i = 36; i<45; i++)
            this.headsInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.headsInventory.setItem(45, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("previous-page-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("pageswitcher-description", String.valueOf(this.headInventoryPage), String.valueOf(this.maxHeadInventoryPages))).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2MTQwYWYzMmNiMzY0ZDliZTNiOTRlOTMwODFkNmNmYzhjMjdkM2NmZTBiNGRkNDVlNzg1MjI1ZWIifX19"));
        this.headsInventory.setItem(53, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("next-page-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("pageswitcher-description", String.valueOf(this.headInventoryPage), String.valueOf(this.maxHeadInventoryPages))).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNhODQyNjdjYjVhMzdkNjk5YWJlN2Q2YTAzMTc4ZGUwODlkN2NmMmU3MjZmMzdkYTNmZTk5N2ZkNyJ9fX0="));

        final int startIndex = ((this.headInventoryPage - 1) * 36);

        for(int i = 0; i<36; i++) {
            if(headValues.size() >= (startIndex + i + 1))
                this.headsInventory.setItem(i, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("heads-headitem-name")).setHeadValueAndBuild(headValues.get(startIndex + i)));
        }
    }

}
