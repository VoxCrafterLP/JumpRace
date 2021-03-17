package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.objects.Map;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 28.02.2021
 * Time: 11:56
 * Project: JumpRace
 */

@Getter
public class InventoryManager {

    private final Inventory lobbyInventory, jumpingInventory, endingInventory, teamSelectorInventory, mapSwitcherInventory;

    public InventoryManager() {
        this.lobbyInventory = Bukkit.createInventory(null, 36);
        this.jumpingInventory = Bukkit.createInventory(null, 36);
        this.endingInventory = Bukkit.createInventory(null, 36);

        this.mapSwitcherInventory = Bukkit.createInventory(null, 54, "§bChoose a map");
        this.teamSelectorInventory = Bukkit.createInventory(null, 27, "§bChoose your team");

        this.buildInventories();
    }

    private void buildInventories() {
        //===============================================//

        this.lobbyInventory.setItem(0, new ItemManager(Material.BED).setDisplayName("§bTeam Selector").addLore("§8§m------------------", " ", "§7Right click to select", "§7a team", " ", "§8§m------------------").build());
        this.lobbyInventory.setItem(8, new ItemManager(Material.MAGMA_CREAM).setDisplayName("§cLeave").addLore("§8§m------------------", " ", "§7Right click to leave", "§7the game", " ", "§8§m------------------").build());

        //===============================================//

        this.jumpingInventory.setItem(0, new ItemManager(Material.WOOD_AXE).setUnbreakable(true).build());
        this.jumpingInventory.setItem(8, new ItemManager(Material.COMPASS).build());

        //===============================================//

        this.endingInventory.setItem(8, new ItemManager(Material.MAGMA_CREAM).setDisplayName("§cLeave").addLore("§8§m------------------", " ", "§7Right click to leave", "§7the game", " ", "§8§m------------------").build());

        //===============================================//

        for(int i = 0; i<9; i++)
            this.teamSelectorInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());
        for(int i = 18; i<27; i++)
            this.teamSelectorInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.updateTeamSelectorInventory();

        //===============================================//

        final List<Map> loadedMaps = JumpRace.getInstance().getLocationManager().getLoadedMaps();
        for(int i = 0; i<loadedMaps.size(); i++) {
            if(i < 53)
                this.mapSwitcherInventory.setItem(i, new ItemManager(Material.SKULL_ITEM,3).setDisplayName("§b" + loadedMaps.get(i).getName()).addLore("§8§m------------------", " ", "§7Click to select", "§7this map", " ", "§8§m------------------").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThkYWExZTNlZDk0ZmYzZTMzZTFkNGM2ZTQzZjAyNGM0N2Q3OGE1N2JhNGQzOGU3NWU3YzkyNjQxMDYifX19"));
        }

        if(loadedMaps.size() != 0)
            this.mapSwitcherInventory.setItem(53, new ItemManager(Material.SKULL_ITEM,3).setDisplayName("§3Random map").addLore("§8§m------------------", " ", "§7Click to select", "§7a random map", " ", "§8§m------------------").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0MzU2NTI4MzU1OTJiNDZhMjQxNDhiNGNhNzQyYTRiZGY4ZjY3OGQ3ZDcwYTM4NzkyNzM4Yjg1Y2QzMyJ9fX0="));

        //===============================================//
    }

    public void setInventory(Player player, Type type) {
        switch (type) {
            case LOBBY:
                player.getInventory().clear();
                player.getInventory().setContents(this.lobbyInventory.getContents());

                if(player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getMapSwitchPermission())
                        || player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getAdminPermission()))
                    player.getInventory().setItem(7, new ItemManager(Material.NETHER_STAR).setDisplayName("§bMap switcher").addLore("§8§m------------------", " ", "§7Right click to switch", "§7the map", " ", "§8§m------------------").build());
                break;
            case JUMPING:
                player.getInventory().clear();
                player.getInventory().setContents(this.jumpingInventory.getContents());
                player.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).setUnbreakable(true).build());
                player.getInventory().setChestplate(new ItemManager(Material.LEATHER_CHESTPLATE).setUnbreakable(true).build());
                player.getInventory().setLeggings(new ItemManager(Material.LEATHER_LEGGINGS).setUnbreakable(true).build());
                player.getInventory().setBoots(new ItemManager(Material.LEATHER_BOOTS).setUnbreakable(true).build());
                break;
            case ENDING:
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().setContents(this.endingInventory.getContents());
                break;
        }
    }

    public void updateTeamSelectorInventory() {
        AtomicInteger i = new AtomicInteger(9);

        JumpRace.getInstance().getGameManager().getRegisteredTeams().forEach(team -> {
            this.teamSelectorInventory.setItem(i.get(), team.getTeamSelectorItem());
            i.getAndIncrement();
        });
    }

    public enum Type {

        LOBBY,
        JUMPING,
        ENDING

    }

}
