package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.ModuleData;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.04.2021
 * Time: 16:42
 * Project: JumpRace
 */

@Getter
public class ParticleManager {

    private final List<ParticleEffect> particleEffects;
    private final Player player;
    private final Location moduleLocation;

    public ParticleManager(Module module, Player player) {
        this.particleEffects = Lists.newCopyOnWriteArrayList();
        this.player = player;
        this.moduleLocation = module.getModuleBorders()[0];

        module.getModuleData().getParticleEffectData().forEach(particleEffectData ->
                this.particleEffects.add(new ParticleEffectBuilder(particleEffectData.getEffectType(),
                particleEffectData.getRelativePosition(), particleEffectData.getParticleType(), this.moduleLocation)
                .setRotation(particleEffectData.getYaw(), particleEffectData.getPitch(), particleEffectData.getRoll())
                .setSize(particleEffectData.getSize()).setVisibleTo(Collections.singletonList(player))
                .setAction(particleEffectData.getAction()).build()));

        this.startEffects();
    }

    public void addParticleEffect(EffectType effectType, RelativePosition relativePosition) {
        final ParticleEffect particleEffect = new ParticleEffectBuilder(effectType, relativePosition, ParticleType.FLAME, this.moduleLocation)
                .setVisibleTo(Collections.singletonList(this.player)).setRotation(0, 0, 0)
                .setSize(1).build();
        this.particleEffects.add(particleEffect);
        particleEffect.startDrawing();
    }

    public void startEffects() {
        new Thread(() -> this.particleEffects.forEach(ParticleEffect::startDrawing)).start();
    }

    public void stopEffects() {
        this.particleEffects.forEach(ParticleEffect::stopDrawing);
    }

}
