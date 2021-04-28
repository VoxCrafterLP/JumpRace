package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.particlesystem.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleEffectData;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.04.2021
 * Time: 16:20
 * Project: JumpRace
 */

@Getter
public abstract class ParticleEffect {

    private final Location location;
    private final EnumParticle enumParticle;
    private final int yaw, pitch, roll, size;
    private final List<Player> visibleTo;

    private int taskID;

    public ParticleEffect(Location location, EnumParticle enumParticle, int yaw, int pitch, int roll, int size, List<Player> visibleTo) {
        this.location = location;
        this.enumParticle = enumParticle;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.size = size;
        this.visibleTo = visibleTo;
    }

    /**
     * Starts a scheduler which calls the {@link ParticleEffect#draw()} method every 4 ticks
     */
    public void startDrawing() {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), this::draw, 10, 4);
    }

    /**
     * Stops the scheduler that is drawing the particle effect
     */
    public void stopDrawing() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public abstract void draw();

    /**
     * Sends a {@link PacketPlayOutWorldParticles} packet to players in a 50 block radius
     * @param packet Packet that should be send
     * @param location Location of the particle effect
     */
    public void sendPacket(PacketPlayOutWorldParticles packet, Location location) {
        this.visibleTo.forEach(player -> {
            if(location.distance(player.getLocation()) <= 50)
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });
    }

    public abstract EffectType getEffectType();

    public abstract ParticleEffectData getEffectData();

}
