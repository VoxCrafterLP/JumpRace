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
    private final ModuleEditorSettings settings;
    private EditorSetup editorSetup;

    public ModuleEditor(Player player, Module module) {
        this.player = player;
        this.module = module;
        this.settings = new ModuleEditorSettings(this);

        JumpRace.getInstance().getEditorSessions().put(this.player, this);
    }

    /**
     * Build the module & teleport the player to the spawn location of the module
     */
    public void startEditor() {
        this.module.build(this.getSpawnLocation(), true);
        this.player.teleport(this.module.getPlayerSpawnLocation());
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-editor-start-1"));
        this.player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-editor-start-2"));

        if(this.settings.getEditorMode() == EditorMode.PERFORMANCE)
            this.module.stopParticles();

        this.giveItems();
        this.startActionbar();
    }

    /**
     * Start the setup in order to get missing information
     */
    public void startEditorSetup() {
        this.editorSetup = new EditorSetup(this);
        this.editorSetup.startSetup();
    }

    /**
     * Restore the player's previous inventory & stop particles.
     * Clear the area of the module
     * @see #clearArea(Location[])
     */
    public void exitEditor() {
        this.resetToPreviousInventory();
        Bukkit.getScheduler().cancelTask(this.actionbarTaskID);
        this.module.stopParticles();

        final Location[] borders = (this.settings.getEditorMode() == EditorMode.QUICK) ? this.module.getModuleBorders() : this.editorSetup.getBorders();

        this.module.saveModule(borders, this.calculateRelativePosition(this.module.getStartPointLocation(), borders[0]), calculateRelativePosition(this.module.getEndPointLocation(), borders[0]));
        this.clearArea(borders);

        JumpRace.getInstance().getEditorSessions().remove(this.player);
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-editor-save-success"));
        new TitleUtil().sendFullTitle(player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("title-editor-save-success-1"), JumpRace.getInstance().getLanguageLoader().getTranslationByKey("title-editor-save-success2"), 10, 35, 5);
    }

    /**
     * Save the player's previous inventory and give the player the settings item
     */
    private void giveItems() {
        this.playerInventory = Bukkit.createInventory(null, 36);
        this.playerInventory.setContents(this.player.getInventory().getContents());
        this.player.getInventory().clear();

        this.player.getInventory().setItem(8, new ItemManager(Material.REDSTONE_COMPARATOR).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("settings-item-name")).build());
    }

    /**
     * Restore the player's previous inventory 
     */
    public void resetToPreviousInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setContents(this.playerInventory.getContents());
    }

    /**
     * @return Default spawn location of the module
     */
    private Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("jumprace"), 0, JumpRace.getInstance().getJumpRaceConfig().getModuleSpawnHeight(), 0);
    }

    /**
     * Start the editing actionbar
     */
    private void startActionbar() {
        this.actionbarTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            new ActionBarUtil().sendActionbar(this.player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-editor-editing", this.module.getName(), this.module.getModuleDifficulty().getDisplayName()));
        }, 5, 20);
    }

    /**
     * Recalculate the module's borders if the quick editor is selected
     * @param moduleBorders Current borders
     * @param location Location of the updated block
     * @param interactAction Type of the interaction
     */
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
                    for(int y = moduleBorders[0].getBlockY(); y < (moduleBorders[1].getBlockY() + 1); y++) {
                        for(int x = moduleBorders[0].getBlockX(); x < (moduleBorders[1].getBlockX() + 1); x++) {
                            if(location.getWorld().getBlockAt(x, y, moduleBorders[0].getBlockZ()).getType() != Material.AIR) {
                                containsBlock = true;
                                break;
                            }
                        }
                    }
                    if(!containsBlock)
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

    /**
     * Fill the area of the module with AIR blocks
     * @param borders Borders of the module
     */
    public void clearArea(Location[] borders) {
        if(borders == null) return;
        for(int x = borders[0].getBlockX(); x < (borders[1].getBlockX() + 1); x++) {
            for(int y = borders[0].getBlockY(); y < (borders[1].getBlockY() + 1); y++) {
                for(int z = borders[0].getBlockZ(); z < (borders[1].getBlockZ() + 1); z++) {
                    borders[0].getWorld().getBlockAt(new Location(borders[0].getWorld(), x, y, z)).setType(Material.AIR);
                }
            }
        }
    }

    /**
     * Calculate a {@link RelativePosition} based on a {@link Location}
     * @param location Location which should be converted to a {@link RelativePosition}
     * @param borderLocation Location of the left lower border
     * @return Calculated RelativePosition
     */
    private RelativePosition calculateRelativePosition(Location location, Location borderLocation) {
        final int relativeX = location.getBlockX() - borderLocation.getBlockX();
        final int relativeY = location.getBlockY() - borderLocation.getBlockY();
        final int relativeZ = location.getBlockZ() - borderLocation.getBlockZ();

        return new RelativePosition(relativeX, relativeY, relativeZ);
    }
}
