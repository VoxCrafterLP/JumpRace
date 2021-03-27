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

    public void addRelativeX() {
        this.relativeX++;
    }

    public void addRelativeY() {
        this.relativeY++;
    }

    public void addRelativeZ() {
        this.relativeZ++;
    }

    public void subtractRelativeX() {
        this.relativeX--;
    }

    public void subtractRelativeY() {
        this.relativeY--;
    }

    public void subtractRelativeZ() {
        this.relativeZ--;
    }

    public RelativePosition getForParticles() {
        this.relativeX++;
        this.relativeY++;
        this.relativeZ++;
        return this;
    }
}
