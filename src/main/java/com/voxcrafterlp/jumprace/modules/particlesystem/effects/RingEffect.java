package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.04.2021
 * Time: 16:29
 * Project: JumpRace
 */

public class RingEffect extends ParticleEffect {

    public RingEffect(Location location, EnumParticle enumParticle, int yaw, int pitch, int roll, int size, List<Player> visibleTo) {
        super(location, enumParticle, yaw, pitch, roll, size, visibleTo);
    }

    @Override
    public void draw() {

    }
}
