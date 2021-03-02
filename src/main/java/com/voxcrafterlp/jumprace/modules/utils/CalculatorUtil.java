package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 21:17
 * Project: JumpRace
 */

public class CalculatorUtil {

    /**
     * Calculates the spawn location of the module based on the last module
     * @param endPoint Endpoint location of the last module
     * @param relativePosition RelativePosition of the module
     * @return Spawn location of the module
     */
    public Location calculateSpawnLocation(Location endPoint, RelativePosition relativePosition) {
        int x = endPoint.getBlockX() - relativePosition.getRelativeX();
        int y = endPoint.getBlockY() - relativePosition.getRelativeY();
        int z = endPoint.getBlockZ() - relativePosition.getRelativeZ();

        return new Location(Bukkit.getWorld("jumprace"), x, y, z);
    }

}
