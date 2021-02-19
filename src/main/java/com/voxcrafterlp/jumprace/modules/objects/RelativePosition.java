package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:47
 * Project: JumpRace
 */

@Getter
public class RelativePosition {

    private int relativeX;
    private int relativeY;
    private int relativeZ;

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

    public RelativePosition(RelativePosition relativePosition) {
        this.relativeX = relativePosition.getRelativeX();
        this.relativeY = relativePosition.getRelativeY();
        this.relativeZ = relativePosition.getRelativeZ();
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("relativeX", this.relativeX).put("relativeY", this.relativeY).put("relativeZ", this.relativeZ);
    }

    public RelativePosition addRelativeX() {
        this.relativeX++;
        return this;
    }

    public RelativePosition addRelativeY() {
        this.relativeY++;
        return this;
    }

    public RelativePosition addRelativeZ() {
        this.relativeZ++;
        return this;
    }

    public RelativePosition subtractRelativeX() {
        this.relativeX--;
        return this;
    }

    public RelativePosition subtractRelativeY() {
        this.relativeY--;
        return this;
    }

    public RelativePosition subtractRelativeZ() {
        this.relativeZ--;
        return this;
    }

    public RelativePosition getForParticles() {
        this.relativeX++;
        this.relativeY++;
        this.relativeZ++;
        return this;
    }
}
