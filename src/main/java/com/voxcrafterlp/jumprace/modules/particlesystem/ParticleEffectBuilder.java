package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.RingEffect;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
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
    private Location location;
    private EnumParticle enumParticle;
    private int yaw, pitch, roll, size;
    private List<Player> visibleTo;

    public ParticleEffectBuilder(EffectType effectType, Location location, EnumParticle enumParticle) {
        this.effectType = effectType;
        this.location = location;
        this.enumParticle = enumParticle;
    }

    public ParticleEffectBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public ParticleEffectBuilder setParticleType(EnumParticle enumParticle) {
        this.enumParticle = enumParticle;
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

    public ParticleEffectBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public ParticleEffectBuilder setVisibleTo(List<Player> players) {
        this.visibleTo = players;
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

        final Class[] typeArray = new Class[]{Location.class, EnumParticle.class, int.class, int.class, int.class,
                int.class, List.class};

        try {
            return effectType.getClazz().getDeclaredConstructor(typeArray).newInstance(this.location, this.enumParticle, this.yaw, this.pitch, this.roll, this.size, this.visibleTo);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.broadcastMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("error-message"));
            e.printStackTrace();
        }
        return null;
    }

    @Getter
    public enum EffectType {

        RING(RingEffect.class);

        private final Class<? extends ParticleEffect> clazz;

        EffectType(Class<? extends ParticleEffect> clazz) {
            this.clazz = clazz;
        }

    }

}
