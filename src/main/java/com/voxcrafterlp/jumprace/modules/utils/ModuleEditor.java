package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.EditorMode;
import com.voxcrafterlp.jumprace.modules.enums.InteractionType;
import com.voxcrafterlp.jumprace.modules.objects.EditorSetup;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.utils.ActionBarUtil;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import com.voxcrafterlp.jumprace.utils.TitleUtil;
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

    public int actionbarTaskID;
    private ModuleEditorSettings settings;
    private EditorSetup editorSetup;

    public ModuleEditor(Player player, Module module) {
        this.player = player;
        this.module = module;
        this.settings = new ModuleEditorSettings(this);

        JumpRace.getInstance().getEditorSessions().put(this.player, this);
    }

    public void startEditor() {
        this.module.build(this.getSpawnLocation(), true);
        this.player.teleport(this.getPlayerSpawnLocation());
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You can now start §bbuilding§8.");
        this.player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You can §cdisable §7particles in the §bsettings by switching to the §cperformance editor§8.");

        if(this.settings.getEditorMode() == EditorMode.PERFORMANCE)
            this.module.stopParticles();

        this.giveItems();
        this.startActionbar();
    }

    public void startEditorSetup() {
        this.editorSetup = new EditorSetup(this);
        this.editorSetup.startSetup();
    }

    public void exitEditor() {
        this.resetToPreviousInventory();
        Bukkit.getScheduler().cancelTask(this.actionbarTaskID);
        this.module.stopParticles();

        final Location[] borders = (this.settings.getEditorMode() == EditorMode.QUICK) ? this.module.getModuleBorders() : this.editorSetup.getBorders();

        this.module.saveModule(borders, calculateRelativePosition(this.module.getStartPointLocation(), borders[0]), calculateRelativePosition(this.module.getEndPointLocation(), borders[0]));
        this.clearArea(borders);

        JumpRace.getInstance().getEditorSessions().remove(this.player);
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The module has been saved §asuccessfully§8.");
        new TitleUtil().sendFullTitle(player, "§7Module", "§asaved", 10, 35, 5);
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

    public void resetToPreviousInventory() {
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
        if(this.settings.getEditorMode() == EditorMode.PERFORMANCE) return;

        if(interactAction == InteractionType.PLACE) {
            if(moduleBorders[1].getBlockX() < location.getBlockX())
                this.module.getBorder2().addRelativeX();

            if(moduleBorders[0].getBlockZ() > location.getBlockZ())
                this.module.getBorder1().subtractRelativeZ();
            if(moduleBorders[1].getBlockZ() < location.getBlockZ())
                this.module.getBorder2().addRelativeZ();

            if(moduleBorders[0].getBlockY() > location.getBlockY())
                this.module.getBorder1().subtractRelativeY();
            if(moduleBorders[1].getBlockY() < location.getBlockY())
                this.module.getBorder2().addRelativeY();
        } else {
            {
                boolean containsBlock = false;
                while (!containsBlock) {
                    moduleBorders = this.module.getModuleBorders();
                    for(int y = moduleBorders[0].getBlockY(); y<(moduleBorders[1].getBlockY() + 1); y++) {
                        for(int z = moduleBorders[0].getBlockZ(); z<(moduleBorders[1].getBlockZ() + 1); z++) {
                            if(location.getWorld().getBlockAt(moduleBorders[1].getBlockX(), y, z).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if(!containsBlock)
                        this.module.getBorder2().subtractRelativeX();
                }
            }
            {
                boolean containsBlock = false;
                while (!containsBlock) {
                    moduleBorders = this.module.getModuleBorders();
                    for(int y = moduleBorders[0].getBlockY(); y<(moduleBorders[1].getBlockY() + 1); y++) {
                        for(int x = moduleBorders[0].getBlockX(); x<(moduleBorders[1].getBlockX() + 1); x++) {
                            if(location.getWorld().getBlockAt(x, y, moduleBorders[1].getBlockZ()).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if(!containsBlock)
                        this.module.getBorder2().subtractRelativeZ();
                }
            }
            {
                boolean containsBlock = false;
                while (!containsBlock) {
                    moduleBorders = this.module.getModuleBorders();
                    for (int y = moduleBorders[0].getBlockY(); y < (moduleBorders[1].getBlockY() + 1); y++) {
                        for (int x = moduleBorders[0].getBlockX(); x < (moduleBorders[1].getBlockX() + 1); x++) {
                            if (location.getWorld().getBlockAt(x, y, moduleBorders[0].getBlockZ()).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if (!containsBlock)
                        this.module.getBorder1().addRelativeZ();
                }
            }
            {
                boolean containsBlock = false;
                while (!containsBlock) {
                    moduleBorders = this.module.getModuleBorders();
                    for(int x = moduleBorders[0].getBlockX(); x<(moduleBorders[1].getBlockX() + 1); x++) {
                        for(int z = moduleBorders[0].getBlockZ(); z<(moduleBorders[1].getBlockZ() + 1); z++) {
                            if(location.getWorld().getBlockAt(x, moduleBorders[1].getBlockY(), z).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if(!containsBlock)
                        this.module.getBorder2().subtractRelativeY();
                }
            }
            {
                boolean containsBlock = false;
                while (!containsBlock) {
                    moduleBorders = this.module.getModuleBorders();
                    for(int x = moduleBorders[0].getBlockX(); x<(moduleBorders[1].getBlockX() + 1); x++) {
                        for(int z = moduleBorders[0].getBlockZ(); z<(moduleBorders[1].getBlockZ() + 1); z++) {
                            if(location.getWorld().getBlockAt(x, moduleBorders[0].getBlockY(), z).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if(!containsBlock)
                        this.module.getBorder1().addRelativeY();
                }
            }
        }
        this.module.recalculateParticles();
    }


    public void clearArea(Location[] borders) {
        for (int x = borders[0].getBlockX(); x < (borders[1].getBlockX() + 1); x++) {
            for (int y = borders[0].getBlockY(); y < (borders[1].getBlockY() + 1); y++) {
                for (int z = borders[0].getBlockZ(); z < (borders[1].getBlockZ() + 1); z++) {
                    borders[0].getWorld().getBlockAt(new Location(borders[0].getWorld(), x, y, z)).setType(Material.AIR);
                }
            }
        }
    }

    private RelativePosition calculateRelativePosition(Location location, Location borderLocation) {
        int relativeX = location.getBlockX() - borderLocation.getBlockX();
        int relativeY = location.getBlockY() - borderLocation.getBlockY();
        int relativeZ = location.getBlockZ() - borderLocation.getBlockZ();

        return new RelativePosition(relativeX, relativeY, relativeZ);
    }
}
