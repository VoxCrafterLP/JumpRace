package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.voxcrafterlp.jumprace.JumpRace;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    public void startDrawing() {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), this::draw, 10, 10);
    }

    public void stopDrawing() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public void draw() {}

}
