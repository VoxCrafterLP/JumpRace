package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.Action;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleEffectData;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import com.voxcrafterlp.jumprace.modules.utils.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 06.08.2021
 * Time: 12:29
 * Project: JumpRace
 */

public class HollowPlateEffect extends ParticleEffect {

    private static final double BASE_PARTICLE_DENSITY = 4;
    private static final double BASE_PARTICLE_DISTANCE = 0.25;

    private final List<Location> particleLocations;

    public HollowPlateEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation, Action action) {
        super(relativePosition, particleType, yaw, pitch, roll, size, visibleTo, moduleLocation, action);
        this.particleLocations = Lists.newCopyOnWriteArrayList();
        super.buildInventory();
    }

    @Override
    public void calculatePositions() {
        this.particleLocations.clear();

        //Calculates the particle amount
        final double particles = super.getSize() * BASE_PARTICLE_DENSITY;
        final double halfSize = super.getSize() / 2.0;

        for(int iX = 0; iX<particles; iX++) {
            for(int iZ = 0; iZ<particles; iZ++) {
                double x = (iX * BASE_PARTICLE_DISTANCE) - halfSize;
                double y = 0;
                double z = (iZ * BASE_PARTICLE_DISTANCE) - halfSize;

                int components = 0;
                if(iX == 0 || iX == (particles - 1)) components++;
                if(iZ == 0 || iZ == (particles - 1)) components++;
                if(components >= 1) {
                    //Apply rotation
                    final Vector vector = new Vector(x, y, z);
                    new MathUtils().rotate(vector, super.getYaw(), super.getPitch(), super.getRoll());
                    this.particleLocations.add(super.getLocation().clone().add(vector));
                }
            }
        }
    }

    @Override
    public void draw() {
        this.particleLocations.forEach(location -> {
            super.sendPacket(new PacketPlayOutWorldParticles(super.getParticleType().getEnumParticle(),
                    true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 255, 0, 0), location);
        });
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.HOLLOW_PLATE;
    }

    @Override
    public ParticleEffectData getEffectData() {
        return new ParticleEffectData(this.getEffectType(), super.getRelativePosition(), super.getParticleType().name(), super.getYaw(), super.getPitch(), super.getRoll(), super.getSize(), super.getAction());
    }

    @Override
    public double getSizeStepAmount() {
        return 0.5;
    }

    @Override
    public double getMaxSize() {
        return 3.5;
    }

    @Override
    public int getParticleSpawnDelay() {
        return 4;
    }
}