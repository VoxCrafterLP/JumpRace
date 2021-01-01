package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.modules.objects.Module;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    public ModuleEditor(Player player, Module module) {
        this.player = player;
        this.module = module;
    }

    public void startEditor() {
        this.module.build(this.getSpawnLocation(), true);

    }


    /**
     * @return Default spawn location
     */
    private Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("jumprace"), 0, 100, 0);
    }

}
