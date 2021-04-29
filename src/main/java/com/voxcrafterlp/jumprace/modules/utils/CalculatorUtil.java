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
     * Calculate a {@link RelativePosition} based on a {@link Location}
     * @param location Location which should be converted to a {@link RelativePosition}
     * @param borderLocation Location of the left lower border
     * @return Calculated RelativePosition
     */
    public RelativePosition calculateRelativePosition(Location location, Location borderLocation) {
        final double relativeX = location.getX() - borderLocation.getBlockX();
        final double relativeY = location.getY() - borderLocation.getBlockY();
        final double relativeZ = location.getZ() - borderLocation.getBlockZ();

        return new RelativePosition(relativeX, relativeY, relativeZ);
    }

    /**
     * Calculates a {@link Location} based on {@link RelativePosition}
     * @param location Lower left corner of the module
     * @param relativePosition RelativePosition which should be converted
     * @return Calculated location
     */
    public Location calculateLocation(Location location, RelativePosition relativePosition) {
        return new Location(location.getWorld(), (location.getX() + relativePosition.getRelativeX()), (location.getY() + relativePosition.getRelativeY()), (location.getZ()) + relativePosition.getRelativeZ());
    }

    /**
     * Calculate the spawn location of the module based on the last module
     * @param endPoint Endpoint location of the last module
     * @param relativePosition RelativePosition of the module
     * @return Spawn location of the module
     */
    public Location calculateSpawnLocation(Location endPoint, RelativePosition relativePosition) {
        final double x = endPoint.getBlockX() - relativePosition.getRelativeX();
        final double y = endPoint.getBlockY() - relativePosition.getRelativeY();
        final double z = endPoint.getBlockZ() - relativePosition.getRelativeZ();

        return new Location(Bukkit.getWorld("jumprace"), x, y, z);
    }

    public int calculatePercent(int total, int current) {
        return current * 100 / total;
    }

}
