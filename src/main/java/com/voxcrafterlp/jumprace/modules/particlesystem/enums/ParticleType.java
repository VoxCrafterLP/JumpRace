package com.voxcrafterlp.jumprace.modules.particlesystem.enums;

import com.voxcrafterlp.jumprace.JumpRace;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.04.2021
 * Time: 17:37
 * Project: JumpRace
 */

@Getter
public enum ParticleType {

    FLAME(EnumParticle.FLAME, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-flame-name")),
    VILLAGER_HAPPY(EnumParticle.VILLAGER_HAPPY, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-villager_happy-name")),
    DRIP_WATER(EnumParticle.DRIP_WATER, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-drip_water-name")),
    CLOUD(EnumParticle.CLOUD, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-cloud-name")),
    CRIT(EnumParticle.CRIT, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-crit-name")),
    REDSTONE(EnumParticle.REDSTONE, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-redstone-name")),
    PORTAL(EnumParticle.PORTAL, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-portal-name")),
    SPELL(EnumParticle.SPELL_WITCH, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-spell-name")),
    FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-fireworks-name")),
    HEART(EnumParticle.HEART, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-heart-name")),
    ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-enchantment_table-name"));

    private final EnumParticle enumParticle;
    private final String displayName;

    ParticleType(EnumParticle enumParticle, String displayName) {
        this.enumParticle = enumParticle;
        this.displayName = displayName;
    }

}
