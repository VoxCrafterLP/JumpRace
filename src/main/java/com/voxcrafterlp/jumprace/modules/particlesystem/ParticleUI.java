package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.modules.objects.Module;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.04.2021
 * Time: 16:38
 * Project: JumpRace
 */

@Getter
public class ParticleUI {

    private final Module module;
    private final Player player;

    private Inventory particleOverviewInventory;
    private int particleOverviewPage, maxParticleOverviewPages;

    public ParticleUI(Module module, Player player) {
        this.module = module;
        this.player = player;
    }

    private void buildInventory() {
        this.particleOverviewInventory = Bukkit.createInventory(null, 54, "§bParticles");
        this.particleOverviewPage = 1;

        this.maxParticleOverviewPages = JumpRace.getInstance().getJumpRaceConfig().getHeadValues().size() / 36;
        if((JumpRace.getInstance().getJumpRaceConfig().getHeadValues().size() % 36) != 0)
            this.maxParticleOverviewPages++;

        this.buildHeadInventory();
    }

    public void updateInventory() {

    }

}
