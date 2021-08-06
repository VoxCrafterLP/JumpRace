package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.Action;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleEffectData;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 05.08.2021
 * Time: 22:35
 * Project: JumpRace
 */

public class SphereEffect extends ParticleEffect {

    private static final int BASE_PARTICLE_DENSITY = 15;
    private static final int BASE_PARTICLE_RINGS = 11;
    private final List<Location> particleLocations;

    public SphereEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation, Action action) {
        super(relativePosition, particleType, yaw, pitch, roll, size, visibleTo, moduleLocation, action);
        this.particleLocations = Lists.newCopyOnWriteArrayList();
        super.buildInventory();
    }

    @Override
    public void calculatePositions() {
        this.particleLocations.clear();

        //Calculates the particle density
        final int particles = (int) super.getSize() * BASE_PARTICLE_DENSITY;
        final int rings = (int) super.getSize() * BASE_PARTICLE_RINGS;

        double r = super.getSize();
        for(double phi = 0; phi <= Math.PI; phi += Math.PI / rings) {
            double y = r * Math.cos(phi) + 1.5;
            for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / particles) {
                double x = r * Math.cos(theta) * Math.sin(phi);
                double z = r * Math.sin(theta) * Math.sin(phi);

                this.particleLocations.add(super.getLocation().clone().add(x, y, z));
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
        return EffectType.SPHERE;
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
        return 2.0;
    }

    @Override
    public int getParticleSpawnDelay() {
        return 4;
    }
}
