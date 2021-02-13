package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.InteractionType;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.utils.ActionBarUtil;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.01.2021
 * Time: 23:39
 * Project: JumpRace
 */

@Getter
public class ModuleEditor {

    private final Player player;
    private final Module module;
    private Inventory playerInventory;

    private int actionbarTaskID;

    public ModuleEditor(Player player, Module module) {
        this.player = player;
        this.module = module;

        JumpRace.getInstance().getEditorSessions().put(this.player, this);
    }

    public void startEditor() {
        this.module.build(this.getSpawnLocation(), true);
        this.player.teleport(this.getPlayerSpawnLocation());
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You can now start §bbuilding§8.");
        this.player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You can §cdisable §7particles in the §bsettings§8.");

        this.giveItems();
        this.startActionbar();
    }

    public void exitEditor() {
        this.resetToPreviousInventory();
        Bukkit.getScheduler().cancelTask(this.actionbarTaskID);

        JumpRace.getInstance().getEditorSessions().remove(this.player);
    }

    /**
     * Saves the player's previous inventory
     */
    private void giveItems() {
        this.playerInventory = Bukkit.createInventory(null, 36);
        this.playerInventory.setContents(this.player.getInventory().getContents());
        this.player.getInventory().clear();

        this.player.getInventory().setItem(8, new ItemManager(Material.REDSTONE_COMPARATOR).setDisplayName("§cSettings").build());
    }

    private void resetToPreviousInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setContents(this.playerInventory.getContents());
    }

    /**
     * @return Default spawn location
     */
    private Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("jumprace"), 0, JumpRace.getInstance().getJumpRaceConfig().getModuleSpawnHeight(), 0);
    }

    private Location getPlayerSpawnLocation() {
        final Location spawnLocation = this.module.calculateLocation(this.getSpawnLocation(), this.module.getStartPoint());
        spawnLocation.setX(spawnLocation.getX() + 0.5);
        spawnLocation.setY(spawnLocation.getY() + 1.0);
        spawnLocation.setZ(spawnLocation.getZ() + 0.5);
        spawnLocation.setYaw(-90F);
        spawnLocation.setPitch(1.0F);

        return spawnLocation;
    }

    private void startActionbar() {
        this.actionbarTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            new ActionBarUtil().sendActionbar(this.player, "§7Editing module§8: §b" + this.module.getName() + " §8● §7Difficulty§8: §b" + this.module.getModuleDifficulty().getDisplayName());
        }, 5, 20);
    }

    public void updateBorders(Location[] moduleBorders, Location location, InteractionType interactAction) {
        if(interactAction == InteractionType.PLACE) {
            if(moduleBorders[1].getBlockX() < location.getBlockX())
                this.module.getBorder2().addRelativeX();
        } else {
            boolean containsBlock = false;
            while (!containsBlock) {
                moduleBorders = this.module.getModuleBorders();
                for(int y = moduleBorders[0].getBlockY(); y<(moduleBorders[1].getBlockY()+ 1); y++) {
                    for(int z = moduleBorders[0].getBlockZ(); z<(moduleBorders[1].getBlockZ()+ 1); z++) {
                        if(location.getWorld().getBlockAt(moduleBorders[1].getBlockX(), y, z).getType() != Material.AIR) {
                            containsBlock = true;
                            this.module.getBorder2().subtractRelativeX();
                            break;
                        }
                    }
                }
            }
        }

        this.module.recalculateParticles();
    }

}
