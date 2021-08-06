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
 * Date: 05.08.2021
 * Time: 22:35
 * Project: JumpRace
 */

public class CubeEffect extends ParticleEffect {

    private static final double BASE_PARTICLE_DENSITY = 0.2;
    private final List<Location> particleLocations;

    public CubeEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation, Action action) {
        super(relativePosition, particleType, yaw, pitch, roll, size, visibleTo, moduleLocation, action);
        this.particleLocations = Lists.newCopyOnWriteArrayList();
        super.buildInventory();
    }

    @Override
    public void calculatePositions() {
        this.particleLocations.clear();

        //Calculates the two corners of the cube
        final double halfSize = super.getSize() / 2.0;
        final RelativePosition corner1 = new RelativePosition(-halfSize, -halfSize, -halfSize);
        final RelativePosition corner2 = new RelativePosition(halfSize, halfSize, halfSize);

        //Calculates the outlines and applies rotation
        this.getHollowCube(corner1, corner2, BASE_PARTICLE_DENSITY).forEach(location -> {
            final Vector vector = new Vector(location.getRelativeX(), location.getRelativeY(), location.getRelativeZ());
            new MathUtils().rotate(vector, super.getYaw(), super.getPitch(), super.getRoll());
            this.particleLocations.add(super.getLocation().clone().add(vector));
        });
    }

    @Override
    public void draw() {
        this.particleLocations.forEach(location -> {
            super.sendPacket(new PacketPlayOutWorldParticles(super.getParticleType().getEnumParticle(),
                    true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 255, 0, 0), location);
        });
    }

    /**
     * Calculates a {@link List} containing the locations of the particles
     * @param corner1 Bottom left corner
     * @param corner2 Upper top corner
     * @param particleDistance Distance between the particles which should be
     * calculated based on the size of the effect to improve performance
     * @return Returns the calculated list
     */
    private List<RelativePosition> getHollowCube(RelativePosition corner1, RelativePosition corner2, double particleDistance) {
        final List<RelativePosition> locations = Lists.newCopyOnWriteArrayList();

        final double minX = Math.min(corner1.getRelativeX(), corner2.getRelativeX());
        final double minY = Math.min(corner1.getRelativeY(), corner2.getRelativeY());
        final double minZ = Math.min(corner1.getRelativeZ(), corner2.getRelativeZ());
        final double maxX = Math.max(corner1.getRelativeX(), corner2.getRelativeX());
        final double maxY = Math.max(corner1.getRelativeY(), corner2.getRelativeY());
        final double maxZ = Math.max(corner1.getRelativeZ(), corner2.getRelativeZ());

        for(double x = minX; x <= maxX; x+=particleDistance) {
            for(double y = minY; y <= maxY; y+=particleDistance) {
                for(double z = minZ; z <= maxZ; z+=particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2)
                        locations.add(new RelativePosition(x, y, z));
                }
            }
        }

        return locations;
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.CUBE;
    }

    @Override
    public ParticleEffectData getEffectData() {
        return new ParticleEffectData(this.getEffectType(), super.getRelativePosition(), super.getParticleType().name(), super.getYaw(), super.getPitch(), super.getRoll(), super.getSize(), super.getAction());
    }

    @Override
    public double getSizeStepAmount() {
        return 1.0;
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
