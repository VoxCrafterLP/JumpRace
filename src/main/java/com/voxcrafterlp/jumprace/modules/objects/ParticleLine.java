package com.voxcrafterlp.jumprace.modules.objects;

import com.voxcrafterlp.jumprace.modules.enums.ParticleDirection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 13.02.2021
 * Time: 18:17
 * Project: JumpRace
 */

@Getter
public class ParticleLine {

    private final Location startLocation;
    private final ParticleDirection particleDirection;
    private final int length;

    public ParticleLine(Location startLocation, ParticleDirection particleDirection, int length) {
        this.startLocation = startLocation;
        this.particleDirection = particleDirection;
        this.length = length;
    }

    /**
     * Calculate the particle's location and send a {@link PacketPlayOutWorldParticles} to every player.
     */
    public void draw() {
        float x = (float) startLocation.getX();
        float y = (float) startLocation.getY();
        float z = (float) startLocation.getZ();

        for(int i = 0; i<(this.length * 10); i++) {
            switch (this.particleDirection) {
                case EAST:
                    x = x + 0.1F;
                    break;
                case WEST:
                    x = x - 0.1F;
                    break;
                case UP:
                    y = y + 0.1F;
                    break;
                case DOWN:
                    y = y - 0.1F;
                    break;
                case SOUTH:
                    z = z + 0.1F;
                    break;
                case NORTH:
                    z = z - 0.1F;
                    break;
            }

            final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, x, y, z, 255, 0, 0, 0, 0, 0);
            Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
        }
    }

}
