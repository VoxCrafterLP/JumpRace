package com.voxcrafterlp.jumprace.modules.particlesystem;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.04.2021
 * Time: 16:47
 * Project: JumpRace
 */

@Getter @Setter
public class ParticleEffectData {

    private final EffectType effectType;
    private final Location location;
    private final EnumParticle enumParticle;
    private final int yaw, pitch, roll, size;

    public ParticleEffectData(EffectType effectType, Location location, String particleType, int yaw, int pitch, int roll, int size) {
        this.effectType = effectType;
        this.location = location;
        this.enumParticle = EnumParticle.valueOf(particleType);
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.size = size;
    }

}
