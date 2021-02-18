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

@Getter
public class ModuleSetup {

    @Getter
    private static final HashMap<Player, ModuleSetup> activeSetups = new HashMap<>();

    private final Player player;
    @Setter
    private String name;
    @Setter
    private String builder;
    @Setter
    private ModuleDifficulty moduleDifficulty;

    private int setupStep;

    public ModuleSetup(Player player) {
        this.player = player;
        this.setupStep = 0;

        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Please type the §bname §7of the module you want to §bcreate§7.");
        activeSetups.put(player, this);
    }

    public void nextStep() {
        this.setupStep++;

        switch (this.setupStep) {
            case 1:
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Please enter the name of the §bbuilder(s) §7of the module. (You can change that later)");
                break;
            case 2:
                Inventory inventory = Bukkit.createInventory(null, 27, "§cModule difficulty");

                for(int i = 0; i<27; i++)
                    inventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE,15).setNoName().build());

                inventory.setItem(10, new ItemManager(Material.WOOL,5).setDisplayName("§aEasy").build());
                inventory.setItem(12, new ItemManager(Material.WOOL,1).setDisplayName("§6Normal").build());
                inventory.setItem(14, new ItemManager(Material.WOOL,14).setDisplayName("§cHard").build());
                inventory.setItem(16, new ItemManager(Material.WOOL,15).setDisplayName("§4Very hard").build());

                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Please choose the §bdifficulty §7of the §bmodule§7.");
                player.openInventory(inventory);
                player.playSound(player.getLocation(), Sound.CHEST_OPEN,3,3);
                break;
            case 3:
                new ModuleEditor(this.player, this.finish()).startEditor();
                break;
        }
    }

    /**
     * Builds a new module
     * @return Dummy module
     */
    private Module finish() {
        activeSetups.remove(this.player);
        this.player.playSound(player.getLocation(), Sound.LEVEL_UP, 1,1);
        return new Module(this.name, this.builder, this.moduleDifficulty, null, null, null, true);
    }

}
