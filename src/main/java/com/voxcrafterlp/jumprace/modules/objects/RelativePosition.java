package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import org.json.JSONObject;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:47
 * Project: JumpRace
 */

@Getter
public class RelativePosition {

    private final int relativeX;
    private final int relativeY;
    private final int relativeZ;

    public RelativePosition(int relativeX, int relativeY, int relativeZ) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeZ = relativeZ;
    }

    public RelativePosition(JSONObject jsonObject) {
        this.relativeX = jsonObject.getInt("relativeX");
        this.relativeY = jsonObject.getInt("relativeY");
        this.relativeZ = jsonObject.getInt("relativeZ");
    }

}
