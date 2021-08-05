package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import org.bukkit.Location;
import org.json.JSONObject;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:47
 * Project: JumpRace
 */

@Getter
public class RelativePosition {

    private double relativeX, relativeY, relativeZ;

    public RelativePosition(double relativeX, double relativeY, double relativeZ) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeZ = relativeZ;
    }

    public RelativePosition(JSONObject jsonObject) {
        this.relativeX = jsonObject.getDouble("relativeX");
        this.relativeY = jsonObject.getDouble("relativeY");
        this.relativeZ = jsonObject.getDouble("relativeZ");
    }

    public RelativePosition(RelativePosition relativePosition) {
        this.relativeX = relativePosition.getRelativeX();
        this.relativeY = relativePosition.getRelativeY();
        this.relativeZ = relativePosition.getRelativeZ();
    }

    /**
     * Important! Only extracts the x, y and z coordinates!
     * It doesn't actually calculate any relative position!
     * @param location Location that should be extracted
     */
    public RelativePosition(Location location) {
        this.relativeX = location.getX();
        this.relativeY = location.getY();
        this.relativeZ = location.getZ();
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

    @Override
    public String toString() {
        return "RelativePosition{" +
                "relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                ", relativeZ=" + relativeZ +
                '}';
    }
}
