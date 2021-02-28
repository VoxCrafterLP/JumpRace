package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.voxcrafterlp.jumprace.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 28.02.2021
 * Time: 11:56
 * Project: JumpRace
 */

public class InventoryManager {

    private final Inventory lobbyInventory, jumpingInventory, endingInventory;

    public InventoryManager() {
        this.lobbyInventory = Bukkit.createInventory(null, 36);
        this.jumpingInventory = Bukkit.createInventory(null, 36);
        this.endingInventory = Bukkit.createInventory(null, 36);

        this.buildInventories();
    }

    private void buildInventories() {
        //===============================================//

        this.lobbyInventory.setItem(0, new ItemManager(Material.BED).setDisplayName("§bTeam Selector").addLore("§8§m------------------", " ", "§7Right click to select", "§7a team", " ", "§8§m------------------").build());
        this.lobbyInventory.setItem(8, new ItemManager(Material.MAGMA_CREAM).setDisplayName("§cLeave").addLore("§8§m------------------", " ", "§7Right click to leave", "§7the game", " ", "§8§m------------------").build());

        //===============================================//

        this.jumpingInventory.setItem(0, new ItemManager(Material.WOOD_AXE).setUnbreakable(true).build());
        this.jumpingInventory.setItem(9, new ItemManager(Material.COMPASS).build());

        //===============================================//

        this.endingInventory.setItem(8, new ItemManager(Material.MAGMA_CREAM).setDisplayName("§cLeave").addLore("§8§m------------------", " ", "§7Right click to leave", "§7the game", " ", "§8§m------------------").build());

        //===============================================//
    }

    public void setInventory(Player player, Type type) {
        switch (type) {
            case LOBBY:
                player.getInventory().clear();
                player.getInventory().setContents(this.lobbyInventory.getContents());
                break;
            case JUMPING:
                player.getInventory().clear();
                player.getInventory().setContents(this.lobbyInventory.getContents());
                player.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).setUnbreakable(true).build());
                player.getInventory().setChestplate(new ItemManager(Material.LEATHER_CHESTPLATE).setUnbreakable(true).build());
                player.getInventory().setLeggings(new ItemManager(Material.LEATHER_LEGGINGS).setUnbreakable(true).build());
                player.getInventory().setBoots(new ItemManager(Material.LEATHER_BOOTS).setUnbreakable(true).build());
                break;
            case ENDING:
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().setContents(this.lobbyInventory.getContents());
                break;
        }
    }


    public enum Type {

        LOBBY,
        JUMPING,
        ENDING

    }

}
