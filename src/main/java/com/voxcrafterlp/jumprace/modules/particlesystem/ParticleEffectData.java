package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import lombok.Getter;
import lombok.Setter;
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
    private final RelativePosition relativePosition;
    private final ParticleType particleType;
    private final int yaw, pitch, roll;
    private final double size;
    private final Action action;

    public ParticleEffectData(EffectType effectType, RelativePosition relativePosition, String particleType, int yaw, int pitch, int roll, double size, Action action) {
        this.effectType = effectType;
        this.relativePosition = relativePosition;
        this.particleType = ParticleType.valueOf(particleType);
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.size = size;
        this.action = action;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("type", this.effectType.name())
                .put("location", this.relativePosition.toJSONObject())
                .put("particleType", this.particleType.name())
                .put("yaw", this.yaw).put("pitch", this.pitch).put("roll", this.roll)
                .put("size", this.size).put("actionType", this.action.getActionType().name())
                .put("actionValue", this.action.getValue());
    }

}
