package com.voxcrafterlp.jumprace.modules.particlesystem.enums;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.05.2021
 * Time: 14:54
 * Project: JumpRace
 */

@Getter
public enum ActionType {

    VELOCITY_BOOST(new ItemManager(Material.PISTON_BASE).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("action-velocity-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("action-velocity-description")).build()),
    JUMP_BOOST(new ItemManager(Material.DIAMOND_BOOTS).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("action-jumpboost-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("action-jumpboost-description")).build()),
    SPEED_BOOST(new ItemManager(Material.SUGAR).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("action-speed-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("action-speed-description")).build());

    private final ItemStack display;

    ActionType(ItemStack display) {
        this.display = display;
    }

}
