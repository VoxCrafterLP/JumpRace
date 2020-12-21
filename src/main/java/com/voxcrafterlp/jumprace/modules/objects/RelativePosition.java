package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import org.bukkit.Location;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:47
 * Project: JumpRace
 */

@Getter
public class RelativePosition {

    private final Location original;
    private final int relativeX;
    private final int relativeY;
    private final int relativeZ;

    public RelativePosition(Location original, int relativeX, int relativeY, int relativeZ) {
        this.original = original;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeZ = relativeZ;
    }

}
