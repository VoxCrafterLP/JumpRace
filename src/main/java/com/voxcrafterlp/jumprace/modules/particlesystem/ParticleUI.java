package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

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

    private final Inventory particleOverviewInventory, addParticleInventory;
    private int particleOverviewPage, maxParticleOverviewPages;

    public ParticleUI(Module module, Player player) {
        this.module = module;
        this.player = player;

        this.particleOverviewInventory = Bukkit.createInventory(null, 54, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-overview-inventory"));
        this.addParticleInventory = Bukkit.createInventory(null, 54, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-addeffect-inventory"));

        this.buildOverviewInventory();
        this.buildAddEffectInventory();
    }

    private void buildOverviewInventory() {
        final List<ParticleEffect> particleEffects = this.module.getParticleManager().getParticleEffects();

        this.particleOverviewPage = 1;

        this.maxParticleOverviewPages = particleEffects.size() / 36;
        if((particleEffects.size() % 36) != 0)
            this.maxParticleOverviewPages++;

        if(this.maxParticleOverviewPages == 0)
            this.maxParticleOverviewPages++;

        for(int i = 36; i<45; i++)
            this.particleOverviewInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.particleOverviewInventory.setItem(45, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("previous-page-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("pageswitcher-description", String.valueOf(this.particleOverviewPage), String.valueOf(this.maxParticleOverviewPages))).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));
        this.particleOverviewInventory.setItem(53, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("next-page-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("pageswitcher-description", String.valueOf(this.particleOverviewPage), String.valueOf(this.maxParticleOverviewPages))).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19"));
        this.particleOverviewInventory.setItem(49, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-overview-addeffect-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-overview-addeffect-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTgxMzZmZmYyZTk2MjM4MTBjNzg0ODZhNmZmZTJmOWFlZTUxNDhmM2IyODJmMmZkMjA3ZDI1NWY1OGUwZDEifX19"));

        final int startIndex = ((this.particleOverviewPage - 1) * 36);

        if(particleEffects.isEmpty()) {
            this.particleOverviewInventory.setItem(13, new ItemManager(Material.BARRIER).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-overview-noeffects-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-overview-noeffects-description")).build());
            return;
        }

        for(int i = 0; i<36; i++) {
            if(particleEffects.size() >= (startIndex + i + 1)) {
                final ParticleEffect particleEffect = particleEffects.get(i);
                this.particleOverviewInventory.setItem(i, new ItemManager(particleEffect.getEffectType().getMaterial()).setDisplayName(particleEffect.getEffectType().getDisplayName()).build());
            }
        }
    }

    private void buildAddEffectInventory() {
        final EffectType[] effectTypes = EffectType.values();

        for(int i = 36; i<45; i++)
            this.addParticleInventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build());

        this.addParticleInventory.setItem(45, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-addeffect-back-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-addeffect-back-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));
        this.addParticleInventory.setItem(49, new ItemManager(Material.BOOK).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-addeffect-info-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-addeffect-info-description")).build());


        for(int i = 0; i<effectTypes.length; i++) {
            this.addParticleInventory.setItem(i, new ItemManager(effectTypes[i].getMaterial())
                    .setDisplayName(effectTypes[i].getDisplayName())
                    .addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-addeffect-item-description"))
                    .build());
            i++;
        }
    }

    public void switchEffectOverviewInventoryPage(int newPage) {
        this.particleOverviewPage = newPage;
        this.particleOverviewInventory.clear();
        this.buildOverviewInventory();
    }

    public void updateOverviewInventory() {
        this.switchEffectOverviewInventoryPage(this.particleOverviewPage);
    }

}
