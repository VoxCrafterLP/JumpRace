package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
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

    private final double radius;
    private static final int BASE_PARTICLES_DENSITY = 20;

    public RingEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation) {
        super(relativePosition, particleType, yaw, pitch, roll, size, visibleTo, moduleLocation);
        this.radius = size;
        super.buildInventory();
    }

    @Override
    public void draw() {
        final int particles = (int) this.radius * BASE_PARTICLES_DENSITY;

        for(double t = 0; t < particles; t+=0.5) {
            final double x = this.radius * Math.sin(t);
            final double y = 0;
            final double z = this.radius * Math.cos(t);

            final Vector vector = new Vector(x, y, z);
            new MathUtils().rotate(vector, super.getYaw(), super.getPitch(), super.getRoll());
            final Location location = super.getLocation().clone().add(vector);

            super.sendPacket(new PacketPlayOutWorldParticles(super.getParticleType().getEnumParticle(), true, (float) location.getX(),
                    (float) location.getY(), (float) location.getZ(), 0, 0, 0, 255, 0, 0),location);
        }
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.RING;
    }

    @Override
    public ParticleEffectData getEffectData() {
        return new ParticleEffectData(this.getEffectType(), super.getRelativePosition(), super.getParticleType().name(), super.getYaw(), super.getPitch(), super.getRoll(), super.getSize());
    }

}
