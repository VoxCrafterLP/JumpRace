package com.voxcrafterlp.jumprace.modules.particlesystem.enums;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.*;
import lombok.Getter;
import org.bukkit.Material;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 27.04.2021
 * Time: 18:03
 * Project: JumpRace
 */

@Getter
public enum EffectType {

    RING(RingEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-ring-name"), Material.EMERALD),
    CUBE(CubeEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-cube-name"), Material.NOTE_BLOCK),
    SPHERE(SphereEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-sphere-name"), Material.SLIME_BALL),
    PLATE(PlateEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-plate-name"), Material.GOLD_PLATE),
    HOLLOW_PLATE(HollowPlateEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-hollow-plate-name"), Material.IRON_PLATE),
    HELIX(HelixEffect.class, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particle-effect-helix-name"), Material.EYE_OF_ENDER);

    private final Class<? extends ParticleEffect> clazz;
    private final String displayName;
    private final Material material;

    EffectType(Class<? extends ParticleEffect> clazz, String displayName, Material material) {
        this.clazz = clazz;
        this.displayName = displayName;
        this.material = material;
    }

}
