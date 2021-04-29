package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.json.JSONObject;

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
    private final ParticleType particleType;
    private final int yaw, pitch, roll;
    private final double size;

    public ParticleEffectData(EffectType effectType, Location location, String particleType, int yaw, int pitch, int roll, double size) {
        this.effectType = effectType;
        this.location = location;
        this.particleType = ParticleType.valueOf(particleType);
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.size = size;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("type", this.effectType.name())
                .put("x", this.location.getX()).put("y", this.location.getY()).put("z", this.location.getZ())
                .put("particleType", this.particleType.name())
                .put("yaw", this.yaw).put("pitch", this.pitch).put("roll", this.roll)
                .put("size", this.size);
    }

}
