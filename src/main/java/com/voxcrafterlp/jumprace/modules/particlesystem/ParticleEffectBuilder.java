package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ActionType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.04.2021
 * Time: 16:27
 * Project: JumpRace
 */

public class ParticleEffectBuilder {

    private final EffectType effectType;
    private RelativePosition relativePosition;
    private ParticleType particleType;
    private int yaw, pitch, roll;
    private double size;
    private List<Player> visibleTo;
    private final Location moduleLocation;
    private Action action;

    public ParticleEffectBuilder(EffectType effectType, RelativePosition relativePosition, ParticleType particleType, Location moduleLocation) {
        this.effectType = effectType;
        this.relativePosition = relativePosition;
        this.particleType = particleType;
        this.moduleLocation = moduleLocation;

        this.action = new Action();
    }

    public ParticleEffectBuilder setRelativePosition(RelativePosition relativePosition) {
        this.relativePosition = relativePosition;
        return this;
    }

    public ParticleEffectBuilder setParticleType(ParticleType particleType) {
        this.particleType = particleType;
        return this;
    }

    public ParticleEffectBuilder setRotation(int yaw, int pitch, int roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        return this;
    }

    public ParticleEffectBuilder setYaw(int yaw) {
        this.yaw = yaw;
        return this;
    }

    public ParticleEffectBuilder setPitch(int pitch) {
        this.pitch = pitch;
        return this;
    }

    public ParticleEffectBuilder setRoll(int roll) {
        this.roll = roll;
        return this;
    }

    public ParticleEffectBuilder setSize(double size) {
        this.size = size;
        return this;
    }

    public ParticleEffectBuilder setVisibleTo(List<Player> players) {
        this.visibleTo = players;
        return this;
    }

    public ParticleEffectBuilder setAction(Action action) {
        this.action = action;
        return this;
    }

    public ParticleEffect build() {
        //Set missing values
        if(this.size == 0)
            this.size = 1;
        if(this.visibleTo == null) {
            this.visibleTo = Lists.newCopyOnWriteArrayList();
            this.visibleTo.addAll(Bukkit.getOnlinePlayers());
        }

        final Class[] typeArray = new Class[]{RelativePosition.class, ParticleType.class, int.class, int.class, int.class,
                double.class, List.class, Location.class, Action.class};

        try {
            return effectType.getClazz().getDeclaredConstructor(typeArray).newInstance(this.relativePosition,
                    this.particleType, this.yaw, this.pitch, this.roll, this.size, this.visibleTo, this.moduleLocation, this.action);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.broadcastMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("error-message"));
            e.printStackTrace();
        }
        return null;
    }

}
