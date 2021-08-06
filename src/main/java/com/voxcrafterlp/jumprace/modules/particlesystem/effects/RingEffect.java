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
 * Date: 24.04.2021
 * Time: 16:29
 * Project: JumpRace
 */

public class RingEffect extends ParticleEffect {

    private static final int BASE_PARTICLE_DENSITY = 20;
    private final List<Location> particleLocations;

    public RingEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation, Action action) {
        super(relativePosition, particleType, yaw, pitch, roll, size, visibleTo, moduleLocation, action);
        this.particleLocations = Lists.newCopyOnWriteArrayList();
        super.buildInventory();
    }

    @Override
    public void calculatePositions() {
        this.particleLocations.clear();

        //Calculates the particle density
        final int particles = (int) super.getSize() * BASE_PARTICLE_DENSITY;

        //Calculates the circle outlines and applies rotation
        for(double t = 0; t < particles; t+=0.5) {
            final double x = super.getSize() * Math.sin(t);
            final double y = 0;
            final double z = super.getSize() * Math.cos(t);

            final Vector vector = new Vector(x, y, z);
            new MathUtils().rotate(vector, super.getYaw(), super.getPitch(), super.getRoll());
            this.particleLocations.add(super.getLocation().clone().add(vector));
        }
    }

    @Override
    public void draw() {
        this.particleLocations.forEach(location -> super.sendPacket(new PacketPlayOutWorldParticles(super.getParticleType().getEnumParticle(),
                true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 255, 0, 0), location));
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.RING;
    }

    @Override
    public ParticleEffectData getEffectData() {
        return new ParticleEffectData(this.getEffectType(), super.getRelativePosition(), super.getParticleType().name(), super.getYaw(), super.getPitch(), super.getRoll(), super.getSize(), super.getAction());
    }

    @Override
    public double getSizeStepAmount() {
        return 0.1;
    }

    @Override
    public double getMaxSize() {
        return 2.5;
    }

    @Override
    public int getParticleSpawnDelay() {
        return 4;
    }
}
