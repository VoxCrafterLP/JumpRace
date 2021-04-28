package com.voxcrafterlp.jumprace.modules.utils;

import org.bukkit.util.Vector;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.04.2021
 * Time: 14:45
 * Project: JumpRace
 */

public class MathUtils {

    /**
     * Calculates the rotation for a {@link com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect} using rotation matrices
     * @param vector Initial vector
     * @param yaw Yaw rotation in degrees
     * @param pitch Pitch rotation in degrees
     * @param roll Roll rotation in degrees
     */
    public void rotate(Vector vector, float yaw, float pitch, float roll) {
        final double yawR = yaw / 180.0 * Math.PI;
        final double pitchR = pitch / 180.0 * Math.PI;
        final double rollR = roll / 180.0 * Math.PI;

        //this.rotateAboutZ(vector, rollR);
        this.rotateAboutX(vector, pitchR);
        this.rotateAboutY(vector, -yawR);
    }

    private void rotateAboutX(Vector vector, double a) {
        final double y = Math.cos(a)*vector.getY() - Math.sin(a)*vector.getZ();
        final double z = Math.sin(a)*vector.getY() + Math.cos(a)*vector.getZ();
        vector.setY(y).setZ(z);
    }

    private void rotateAboutY(Vector vector, double b) {
        final double x = Math.cos(b)*vector.getX() + Math.sin(b)*vector.getZ();
        final double z = -Math.sin(b)*vector.getX() + Math.cos(b)*vector.getZ();
        vector.setX(x).setZ(z);
    }

    private void rotateAboutZ(Vector vector, double c) {
        final double x = Math.cos(c)*vector.getX() - Math.sin(c)*vector.getY();
        final double y = Math.sin(c)*vector.getX() + Math.cos(c)*vector.getY();
        vector.setX(x).setY(y);
    }

}
