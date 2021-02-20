package com.voxcrafterlp.jumprace.modules.utils;

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

    private void buildInventory() {
        this.settingsInventory = Bukkit.createInventory(null, 27, "§cSettings");

        for(int i = 0; i<27; i++)
            this.settingsInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.settingsInventory.setItem(10, new ItemManager(Material.NAME_TAG).setDisplayName("§cRename module").addLore("§8§m------------------", " ", "§7Here you can rename", "§7the module", " ", "§8§m------------------").build());
        this.settingsInventory.setItem(12, new ItemManager(Material.ANVIL).setDisplayName("§cChange difficulty").addLore("§8§m------------------", " ", "§7Here you can change the", "§7difficulty of the module", " ", "§8§m------------------").build());
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? "§aQuick editor" : "§cPerformance editor").addLore("§8§m------------------", " ", "§7Here you can change", "§7the editor mode", " ", "§8§m------------------").build());
        this.settingsInventory.setItem(16, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§2Save").addLore("§8§m------------------", " ", "§7Here you can save the", "§7module and exit the editor", " ", "§8§m------------------").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UyYTUzMGY0MjcyNmZhN2EzMWVmYWI4ZTQzZGFkZWUxODg5MzdjZjgyNGFmODhlYThlNGM5M2E0OWM1NzI5NCJ9fX0="));
        this.settingsInventory.setItem(26, new ItemManager(Material.BARRIER).setDisplayName("§4Cancel").addLore("§8§m------------------", " ", "§7Clicking here will close the", "§7editor without saving", " ", "§8§m------------------").build());
    }

    public void updateInventory() {
        this.settingsInventory.setItem(14, new ItemManager((this.editorMode == EditorMode.PERFORMANCE) ? Material.DIODE : Material.REDSTONE_COMPARATOR).setDisplayName((this.editorMode == EditorMode.PERFORMANCE) ? "§aQuick editor" : "§cPerformance editor").addLore("§8§m------------------", " ", "§7Here you can change", "§7the editor mode", " ", "§8§m------------------").build());
    }

    public void openInventory() {
        this.player.openInventory(this.settingsInventory);
        this.player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10, 10);
    }

}
