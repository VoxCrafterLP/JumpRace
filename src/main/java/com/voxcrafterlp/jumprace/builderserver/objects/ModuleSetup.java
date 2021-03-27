package com.voxcrafterlp.jumprace.builderserver.objects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 02:24
 * Project: JumpRace
 */

@Getter @Setter
public class ModuleSetup {

    @Getter
    private static final HashMap<Player, ModuleSetup> activeSetups = new HashMap<>();

    private final Player player;
    private String name;
    private String builder;
    private ModuleDifficulty moduleDifficulty;

    private int setupStep;

    public ModuleSetup(Player player) {
        this.player = player;
        this.setupStep = 0;

        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("module-setup-enter-name"));
        activeSetups.put(player, this);
    }

    public void nextStep() {
        this.setupStep++;

        switch (this.setupStep) {
            case 1:
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("module-setup-enter-builders"));
                break;
            case 2:
                final Inventory inventory = Bukkit.createInventory(null, 27, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-difficulty-inventory-name"));

                for(int i = 0; i<27; i++)
                    inventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE,15).setNoName().build());

                inventory.setItem(10, new ItemManager(Material.WOOL,5).setDisplayName(ModuleDifficulty.EASY.getDisplayName()).build());
                inventory.setItem(12, new ItemManager(Material.WOOL,1).setDisplayName(ModuleDifficulty.NORMAL.getDisplayName()).build());
                inventory.setItem(14, new ItemManager(Material.WOOL,14).setDisplayName(ModuleDifficulty.HARD.getDisplayName()).build());
                inventory.setItem(16, new ItemManager(Material.WOOL,15).setDisplayName(ModuleDifficulty.VERY_HARD.getDisplayName()).build());

                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("module-setup-choose-difficulty"));
                player.openInventory(inventory);
                player.playSound(player.getLocation(), Sound.CHEST_OPEN,3,3);
                break;
            case 3:
                new ModuleEditor(this.player, this.finish()).startEditor();
                break;
        }
    }

    /**
     * Build a new module
     * @return Dummy module
     */
    private Module finish() {
        activeSetups.remove(this.player);
        this.player.playSound(player.getLocation(), Sound.LEVEL_UP, 1,1);
        return new Module(this.name, this.builder, this.moduleDifficulty, null, null, null, true);
    }

}
